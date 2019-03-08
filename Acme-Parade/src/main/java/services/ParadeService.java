
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

import repositories.ParadeRepository;
import domain.Brotherhood;
import domain.Finder;
import domain.Float;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class ParadeService {

	// Managed repository --------------------------

	@Autowired
	private ParadeRepository	paradeRepository;

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


	// Constructors -------------------------------

	public ParadeService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Parade create() {
		Parade result;

		result = new Parade();
		result.setTicker("000000-XXXXX");
		result.setFloats(Collections.<Float> emptySet());

		return result;
	}
	public Parade save(final Parade parade, final int rowLimit, final int columnLimit) {
		Assert.notNull(parade);

		final Parade result;
		Brotherhood brotherhood;
		Date fechaActual;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getArea() != null);

		if (!parade.getIsFinalMode())
			try {
				fechaActual = this.utilityService.current_moment();
				Assert.isTrue(parade.getMoment().after(fechaActual));

			} catch (final Exception e) {
				throw new IllegalArgumentException("Invalid moment");
			}

		if (parade.getId() == 0) {
			parade.setTicker(this.utilityService.generateValidTicker(parade.getMoment()));

			final Integer[][] matrizParade;

			matrizParade = new Integer[rowLimit][columnLimit];

			for (int i = 0; i < matrizParade.length; i++)
				for (int j = 0; j < matrizParade[0].length; j++)
					matrizParade[i][j] = 0;
			parade.setMatrizParade(matrizParade);
		}

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

	public Parade findOneToDisplay(final int paradeId) {
		Parade result;

		result = this.paradeRepository.findOne(paradeId);

		Assert.notNull(result);
		Assert.isTrue(result.getIsFinalMode());

		return result;
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

	protected void searchParadeFinder(final Finder finder, final Pageable pageable) {
		Page<Parade> parades;

		parades = this.paradeRepository.searchParadeFinder(finder.getKeyword(), finder.getArea(), finder.getMinimumDate(), finder.getMaximumDate(), pageable);
		Assert.notNull(parades);

		finder.setParades(parades.getContent());
		finder.setUpdatedMoment(this.utilityService.current_moment());
	}

	// Other business methods ---------------------
	public Collection<Parade> findPublishedParade() {
		Collection<Parade> result;

		result = this.paradeRepository.findPublishedParade();

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

	protected String existTicker(final String ticker) {
		String result;

		result = this.paradeRepository.existTicker(ticker);

		return result;
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
	public Collection<Parade> findParadeByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeByBrotherhood(id);

		return parades;
	}

	public Collection<Parade> findParadeFinalByBrotherhood(final int id) {
		Collection<Parade> parades;

		parades = this.paradeRepository.findParadeFinalByBrotherhood(id);

		return parades;
	}
	public void makeFinal(final Parade parade) {
		Brotherhood principal;
		Brotherhood owner;

		principal = this.brotherhoodService.findByPrincipal();
		owner = this.getBrotherhoodToParade(parade);

		Assert.isTrue(owner.equals(principal));

		parade.setIsFinalMode(true);
		this.messageService.notificationPublishedParade(parade);
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

}