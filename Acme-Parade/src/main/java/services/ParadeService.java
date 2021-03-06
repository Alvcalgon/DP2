
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Finder;
import domain.Float;
import domain.Parade;
import domain.Segment;

@Service
@Transactional
public class ParadeService {

	// Managed repository --------------------------

	@Autowired
	private ParadeRepository		paradeRepository;

	// Other supporting services -------------------

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private FloatService			floatService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private Validator				validator;


	// Constructors -------------------------------

	public ParadeService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Parade create() {
		Parade result;
		final Integer[][] matrizParade;
		final int rowLimit;
		final int columnLimit;

		result = new Parade();
		result.setTicker("000000-XXXXX");
		result.setFloats(Collections.<Float> emptySet());
		result.setSegments(new TreeSet<Segment>());
		rowLimit = this.customisationService.find().getRowLimit();
		columnLimit = this.customisationService.find().getColumnLimit();
		matrizParade = new Integer[rowLimit][columnLimit];

		for (int i = 0; i < matrizParade.length; i++)
			for (int j = 0; j < matrizParade[0].length; j++)
				matrizParade[i][j] = 0;

		result.setMatrizParade(matrizParade);
		result.setStatus("");

		return result;
	}

	public Parade save(final Parade parade) {
		Assert.notNull(parade);
		this.floatService.checkFloatByPrincipal(parade.getFloats().iterator().next());

		final Parade result;
		Brotherhood brotherhood;
		Date fechaActual;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getArea() != null);

		if (!parade.getIsFinalMode())
			Assert.isTrue(parade.getStatus().equals(""));
		else {
			Assert.isTrue(!parade.getStatus().equals("rejected"));
			Assert.isTrue(!parade.getStatus().equals("accepted"));
		}
		try {
			fechaActual = this.utilityService.current_moment();
			Assert.isTrue(parade.getMoment().after(fechaActual));

		} catch (final Exception e) {
			throw new IllegalArgumentException("Invalid moment");
		}

		if (parade.getId() == 0)
			parade.setTicker(this.utilityService.generateValidTicker(parade.getMoment()));
		else
			this.checkBrotherhoodByParade(parade);

		result = this.paradeRepository.save(parade);

		return result;
	}
	public Parade saveRejected(final Parade parade) {
		Assert.notNull(parade);

		Assert.isTrue(parade.getStatus().equals("submitted"));
		final Parade result;
		this.checkChapter(parade);

		if (parade.getReasonWhy() == null)
			throw new DataIntegrityViolationException("ReasonWhy not blank");

		parade.setStatus("rejected");
		result = this.paradeRepository.save(parade);

		return result;
	}
	public void delete(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));
		this.checkBrotherhoodByParade(parade);

		this.sponsorshipService.removeSponsorshipFromParade(parade);
		this.requestService.deleteRequestToParade(parade);
		this.finderService.deleteFromFinders(parade);

		this.paradeRepository.delete(parade);

	}
	public Parade findOne(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		return result;
	}

	public Collection<Parade> findAll() {
		Collection<Parade> results;

		results = this.paradeRepository.findAll();

		return results;
	}

	public Parade findOneToEdit(final int paradeId) {
		Parade result;
		Brotherhood brotherhood;

		result = this.paradeRepository.findOne(paradeId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.brotherhoodService.getBrotherhoodToParade(result).equals(brotherhood));
		Assert.isTrue(!result.getStatus().equals("rejected"));
		Assert.isTrue(!result.getStatus().equals("accepted"));

		return result;
	}

	public Parade findOneToDelete(final int paradeId) {
		Parade result;
		Brotherhood brotherhood;

		result = this.paradeRepository.findOne(paradeId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.brotherhoodService.getBrotherhoodToParade(result).equals(brotherhood));

		return result;
	}
	public Parade findOneToEditChapter(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		Assert.isTrue(result.getStatus().equals("submitted"));
		Assert.notNull(result);
		this.checkChapter(result);

		return result;
	}

	public Parade findOneToDisplay(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		Assert.notNull(result);
		Assert.isTrue(result.getIsFinalMode() && result.getStatus().equals("accepted"));

		return result;
	}
	public Parade findOneToDisplayToChapter(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		Assert.notNull(result);
		Assert.isTrue(result.getIsFinalMode());
		this.checkChapter(result);

		return result;
	}

	// Other business methods ---------------------

	public void makeFinal(final Parade parade) {
		this.checkBrotherhoodByParade(parade);

		parade.setIsFinalMode(true);
		parade.setStatus("submitted");
		this.messageService.notificationPublishedParade(parade);
	}

	public Parade makeCopy(final Parade parade) {
		this.checkBrotherhoodByParade(parade);

		Parade paradeCopied;
		Parade paradeSaved;
		final Integer[][] matrizParade;
		final int rowLimit;
		final int columnLimit;

		paradeCopied = this.create();

		paradeCopied.setTicker(this.utilityService.generateValidTicker(parade.getMoment()));
		paradeCopied.setIsFinalMode(false);
		paradeCopied.setStatus("");
		paradeCopied.setDescription(parade.getDescription());
		paradeCopied.setMoment(parade.getMoment());
		paradeCopied.setFloats(parade.getFloats());
		paradeCopied.setMatrizParade(parade.getMatrizParade());
		paradeCopied.setReasonWhy(null);
		paradeCopied.setTitle(parade.getTitle());

		rowLimit = this.customisationService.find().getRowLimit();
		columnLimit = this.customisationService.find().getColumnLimit();
		matrizParade = new Integer[rowLimit][columnLimit];

		for (int i = 0; i < matrizParade.length; i++)
			for (int j = 0; j < matrizParade[0].length; j++)
				matrizParade[i][j] = 0;

		paradeCopied.setMatrizParade(matrizParade);

		paradeSaved = this.paradeRepository.save(paradeCopied);

		return paradeSaved;

	}

	public Parade accept(final Parade parade) {
		this.checkChapter(parade);
		Assert.notNull(parade);
		Assert.isTrue(parade.getStatus().equals("submitted"));

		parade.setStatus("accepted");

		return parade;
	}

	//	Returns the parades that have FinalMode = true
	public Collection<Parade> findPublishedParade() {
		Collection<Parade> result;

		result = this.paradeRepository.findPublishedParade();

		return result;
	}

	//Returns the parades that have FinalMode = false
	public Collection<Parade> findParadeNotFinalParadeByBrotherhood(final int id) {
		Collection<Parade> result;

		result = this.paradeRepository.findParadeNotFinalParadeByBrotherhood(id);

		return result;
	}

	// Returns the parades whose owner is a brotherhood
	public Collection<Parade> findParadeByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have FinalMode = true
	public Collection<Parade> findParadeFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeFinalByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have status = SUBMITTED
	public Collection<Parade> findParadeSubmittedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeSubmittedByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have status = REJECTED
	public Collection<Parade> findParadeRejectedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeRejectedByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have status =  ACCEPTED
	public Collection<Parade> findParadeAcceptedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeAcceptedByBrotherhood(id);

		return parades;
	}
	//Returns the parades that a brotherhood have status = SUBMITTED and finalMode = true
	public Collection<Parade> findParadeSubmittedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeSubmittedFinalByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have status = REJECTED and finalMode = true
	public Collection<Parade> findParadeRejectedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeRejectedFinalByBrotherhood(id);

		return parades;
	}

	//Returns the parades that a brotherhood have status = ACCEPTED and finalMode = true
	public Collection<Parade> findParadeAcceptedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeAcceptedFinalByBrotherhood(id);

		return parades;
	}

	//Returns the parades that are in the same area and have status = SUBMITTED and finalMode = true
	public Collection<Parade> findSubmittedByArea(final int id) {
		Collection<Parade> parades;

		this.areaService.checkPrincipalArea(id);

		parades = this.paradeRepository.findSubmittedByArea(id);

		return parades;
	}

	//Returns the parades that are in the same area and have status = REJECTED and finalMode = true
	public Collection<Parade> findRejectedByArea(final int id) {
		Collection<Parade> parades;

		this.areaService.checkPrincipalArea(id);

		parades = this.paradeRepository.findRejectedByArea(id);

		return parades;
	}

	//Returns the parades that are in the same area and have status = ACCEPTED and finalMode = true
	public Collection<Parade> findAcceptedByArea(final int id) {
		Collection<Parade> parades;

		this.areaService.checkPrincipalArea(id);

		parades = this.paradeRepository.findAcceptedByArea(id);

		return parades;
	}

	public Parade findBySegment(final int id) {
		Parade parade;

		parade = this.paradeRepository.findBySegment(id);
		Assert.notNull(parade);

		return parade;
	}

	public Boolean floatBelongtToParade(final int floatId) {
		Boolean res;
		Collection<Parade> parades;

		parades = this.paradeRepository.floatBelongtToParade(floatId);
		if (parades.size() == 0)
			res = true;
		else
			res = false;

		return res;

	}

	protected void addSegment(final Parade parade, final Segment segment) {
		parade.getSegments().add(segment);
	}

	protected void searchParadeFinder(final Finder finder, final Pageable pageable) {
		Page<Parade> parades;

		parades = this.paradeRepository.searchParadeFinder(finder.getKeyword(), finder.getArea(), finder.getMinimumDate(), finder.getMaximumDate(), pageable);
		Assert.notNull(parades);

		finder.setParades(parades.getContent());
		finder.setUpdatedMoment(this.utilityService.current_moment());
	}
	//Compruebo que el chapter que cambia el estado sea el que gestiona el area del desfile
	protected void checkChapter(final Parade parade) {
		final Chapter chapter;
		final Area areaChapter;
		Area areaParade;

		chapter = this.chapterService.findByPrincipal();
		areaChapter = chapter.getArea();
		areaParade = this.areaService.findAreaByParade(parade.getId());

		Assert.isTrue(areaChapter.equals(areaParade));
	}
	protected String existTicker(final String ticker) {
		String result;

		result = this.paradeRepository.existTicker(ticker);

		return result;
	}

	public Parade reconstructSave(final Parade parade, final BindingResult binding) {
		Parade result, paradeStored;

		if (parade.getId() != 0) {
			result = new Parade();
			paradeStored = this.findOne(parade.getId());
			result.setTicker(paradeStored.getTicker());
			result.setIsFinalMode(paradeStored.getIsFinalMode());
			result.setMatrizParade(paradeStored.getMatrizParade());
			result.setStatus(paradeStored.getStatus());
			result.setReasonWhy(paradeStored.getReasonWhy());
			result.setVersion(paradeStored.getVersion());
			result.setSegments(paradeStored.getSegments());

		} else
			result = this.create();

		result.setId(parade.getId());
		result.setTitle(parade.getTitle());
		result.setDescription(parade.getDescription());
		result.setMoment(parade.getMoment());
		result.setFloats(parade.getFloats());

		this.validator.validate(result, binding);

		return result;
	}

	public Parade reconstructReject(final Parade parade, final BindingResult binding) {
		Parade result, paradeStored;

		result = new Parade();
		paradeStored = this.findOneToEditChapter(parade.getId());

		result.setId(parade.getId());
		result.setTicker(paradeStored.getTicker());
		result.setTitle(paradeStored.getTitle());
		result.setDescription(paradeStored.getDescription());
		result.setMoment(paradeStored.getMoment());
		result.setIsFinalMode(paradeStored.getIsFinalMode());
		result.setMatrizParade(paradeStored.getMatrizParade());
		result.setStatus(paradeStored.getStatus());
		result.setReasonWhy(parade.getReasonWhy());
		result.setVersion(paradeStored.getVersion());
		result.setFloats(paradeStored.getFloats());
		result.setSegments(paradeStored.getSegments());

		this.checkReasonWhy(result, binding);

		this.validator.validate(result, binding);

		return result;
	}
	private void checkReasonWhy(final Parade parade, final BindingResult binding) {
		if (parade.getReasonWhy().isEmpty())
			binding.rejectValue("reasonWhy", "parade.commit.reasonWhy");
	}

	public SortedMap<Integer, List<Integer>> positionsFree(final Parade parade) {
		SortedMap<Integer, List<Integer>> results;
		Integer numPosition;
		Integer row, column = 0;
		List<Integer> ls;
		final Integer[][] matrizParade;

		results = new TreeMap<>();
		matrizParade = parade.getMatrizParade();
		numPosition = 1;
		for (int i = 0; i < matrizParade.length; i++)
			for (int j = 0; j < matrizParade[0].length; j++)
				if (matrizParade[i][j] == 0) {
					ls = new ArrayList<Integer>();
					row = i + 1;
					column = j + 1;
					ls.add(row);
					ls.add(column);
					results.put(numPosition, ls);
					numPosition++;
				}

		return results;

	}

	public Parade addToMatriz(final Parade parade, final Integer rowParade, final Integer columnParade) {
		Assert.notNull(parade);

		Parade result;
		Integer[][] matriz;

		matriz = parade.getMatrizParade();
		matriz[rowParade - 1][columnParade - 1] = 1;
		parade.setMatrizParade(matriz);

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Parade removeToMatriz(final Parade parade, final Integer rowParade, final Integer columnParade) {
		Assert.notNull(parade);

		Parade result;
		Integer[][] matriz;

		matriz = parade.getMatrizParade();
		matriz[rowParade - 1][columnParade - 1] = 0;
		parade.setMatrizParade(matriz);

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Double[] findDataNumberResultsPerFinder() {
		Double[] result;

		result = this.paradeRepository.findDataNumberResultsPerFinder();
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findParadeLess30days() {
		Date today;
		final Date more30days;
		Collection<Parade> parades;
		Calendar calendar;

		calendar = Calendar.getInstance();
		today = this.utilityService.current_moment();

		calendar.setTime(today);
		calendar.add(Calendar.DAY_OF_YEAR, 30);

		more30days = calendar.getTime();
		parades = this.paradeRepository.findParadeLess30days(today, more30days);

		return parades;

	}

	protected void checkParadeByBrotherhood(final Parade parade) {
		Brotherhood owner;
		Brotherhood principal;

		owner = this.brotherhoodService.findBrotherhoodByParade(parade.getId());
		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(owner.equals(principal));
	}

	// Req 8.1.4 Acme-Parade Ratio parades in draft mode vs final mode
	public Double findRatioParadesDraftModeVSParadesFinalMode() {
		Double result;

		result = this.paradeRepository.findRatioParadesDraftModeVSParadesFinalMode();

		return result;
	}

	// Req 8.1.5 Acme-Parade status = 'submitted'
	public Double findRatioSubmittedParadesFinalMode() {
		Double result;

		result = this.paradeRepository.findRatioSubmittedParadesFinalMode();

		return result;
	}

	// Req 8.1.5 Acme-Parade status = 'accepted'
	public Double findRatioAcceptedParadesFinalMode() {
		Double result;

		result = this.paradeRepository.findRatioAcceptedParadesFinalMode();

		return result;
	}

	// Req 8.1.5 Acme-Parade status = 'rejected'
	public Double findRatioRejectedParadesFinalMode() {
		Double result;

		result = this.paradeRepository.findRatioRejectedParadesFinalMode();

		return result;
	}

	// Req 8.1.2 Acme-Parade. Average, Min, Max, Stdev number of parades coordinated by the chapters
	public Double[] findDataNumerParadesCoordinatedByChapters() {
		Double[] result;

		result = this.paradeRepository.findDataNumerParadesCoordinatedByChapters();

		return result;
	}

	public Double avgNumberParadesCoordinatedByChapters() {
		Double result;

		result = this.paradeRepository.avgNumberParadesCoordinatedByChapters();

		return result;
	}

	protected void deleteParadeBrotherhood(final Brotherhood brotherhood) {
		Collection<Parade> parades;

		parades = this.findParadeByBrotherhood(brotherhood.getId());
		for (final Parade p : parades) {
			this.finderService.deleteFromFinders(p);
			this.sponsorshipService.removeSponsorshipFromParade(p);
		}

		this.paradeRepository.delete(parades);
	}
	private void checkBrotherhoodByParade(final Parade parade) {
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.brotherhoodService.getBrotherhoodToParade(parade).equals(brotherhood));

	}
	public Parade findOneCheckPrincipal(final int id) {
		Parade parade;

		parade = this.paradeRepository.findOne(id);
		this.checkBrotherhoodByParade(parade);
		return parade;
	}
}
