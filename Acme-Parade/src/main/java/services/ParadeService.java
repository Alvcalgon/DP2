
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
import domain.Request;
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
		result.setSegments(Collections.<Segment> emptySet());
		rowLimit = this.customisationService.find().getRowLimit();
		columnLimit = this.customisationService.find().getColumnLimit();
		matrizParade = new Integer[rowLimit][columnLimit];

		for (int i = 0; i < matrizParade.length; i++)
			for (int j = 0; j < matrizParade[0].length; j++)
				matrizParade[i][j] = 0;

		result.setMatrizParade(matrizParade);
		result.setStatus("submitted");

		return result;
	}

	public Parade save(final Parade parade) {
		Assert.notNull(parade);

		final Parade result;
		Brotherhood brotherhood;
		Date fechaActual;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getArea() != null);

		if (!parade.getIsFinalMode())
			Assert.isTrue(parade.getStatus().equals("submitted"));
		try {
			fechaActual = this.utilityService.current_moment();
			Assert.isTrue(parade.getMoment().after(fechaActual));

		} catch (final Exception e) {
			throw new IllegalArgumentException("Invalid moment");
		}

		if (parade.getId() == 0)
			parade.setTicker(this.utilityService.generateValidTicker(parade.getMoment()));

		result = this.paradeRepository.save(parade);

		return result;
	}

	public Parade saveRejected(final Parade parade) {
		Assert.notNull(parade);

		final Parade result;
		this.checkChapter(parade);

		result = this.paradeRepository.save(parade);

		return result;
	}

	public void delete(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(this.paradeRepository.exists(parade.getId()));

		Brotherhood brotherhood;
		Collection<Request> requests;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.getBrotherhoodToParade(parade).equals(brotherhood));

		requests = this.requestService.findRequestByParade(parade.getId());

		for (final Request r : requests)
			this.requestService.deleteRequest(r);

		this.finderService.removeParadeToFinder(parade);

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
		Assert.isTrue(this.getBrotherhoodToParade(result).equals(brotherhood));

		return result;
	}
	public Parade findOneToEditChapter(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

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
		Brotherhood principal;
		Brotherhood owner;

		principal = this.brotherhoodService.findByPrincipal();
		owner = this.getBrotherhoodToParade(parade);

		Assert.isTrue(owner.equals(principal));

		parade.setIsFinalMode(true);
		parade.setStatus("submitted");
		this.messageService.notificationPublishedParade(parade);
	}

	public Parade accept(final Parade parade) {
		this.checkChapter(parade);
		Assert.notNull(parade);
		Assert.isTrue(parade.getStatus().equals("submitted"));

		parade.setStatus("accepted");

		return parade;
	}

	//Devuelve los desfiles que tienen FinalMode = true
	public Collection<Parade> findPublishedParade() {
		Collection<Parade> result;

		result = this.paradeRepository.findPublishedParade();

		return result;
	}

	//Devuelve los desfiles de un brotherhood
	public Collection<Parade> findParadeByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood que tienen FinalMode = true
	public Collection<Parade> findParadeFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeFinalByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood con estado SUBMITTED
	public Collection<Parade> findParadeSubmittedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeSubmittedByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood con estado REJECTED
	public Collection<Parade> findParadeRejectedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeRejectedByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood con estado ACCEPTED
	public Collection<Parade> findParadeAcceptedByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeAcceptedByBrotherhood(id);

		return parades;
	}
	//Devuelve los desfiles de un brotherhood con estado SUBMITTED y MODOFINAL
	public Collection<Parade> findParadeSubmittedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeSubmittedFinalByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood con estado REJECTED y MODOFINAL
	public Collection<Parade> findParadeRejectedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeRejectedFinalByBrotherhood(id);

		return parades;
	}

	//Devuelve los desfiles de un brotherhood con estado ACCEPTED y MODOFINAL
	public Collection<Parade> findParadeAcceptedFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeAcceptedFinalByBrotherhood(id);

		return parades;
	}

	private Brotherhood getBrotherhoodToParade(final Parade parade) {
		Brotherhood result;
		Collection<Float> floats;
		Float floatt;

		floats = parade.getFloats();
		floatt = floats.iterator().next();
		result = floatt.getBrotherhood();

		return result;

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

	protected void searchParadeFinder(final Finder finder, final Pageable pageable) {
		Page<Parade> parades;

		parades = this.paradeRepository.searchParadeFinder(finder.getKeyword(), finder.getArea(), finder.getMinimumDate(), finder.getMaximumDate(), pageable);
		Assert.notNull(parades);

		finder.setParades(parades.getContent());
		finder.setUpdatedMoment(this.utilityService.current_moment());
	}
	//Compruebo que el chapter que cambia el estado sea el que gestiona el area del desfile
	private void checkChapter(final Parade parade) {
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

	public Parade reconstruct(final Parade parade, final BindingResult binding) {
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
		result.setStatus("rejected");
		result.setReasonWhy(parade.getReasonWhy());
		result.setVersion(paradeStored.getVersion());
		result.setFloats(paradeStored.getFloats());
		result.setSegments(paradeStored.getSegments());

		this.validator.validate(result, binding);

		return result;
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

}
