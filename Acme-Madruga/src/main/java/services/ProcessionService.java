
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProcessionRepository;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;
import domain.Request;

@Service
@Transactional
public class ProcessionService {

	// Managed repository --------------------------

	@Autowired
	private ProcessionRepository	processionRepository;

	// Other supporting services -------------------

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private RequestService			requestService;

	@Autowired
	private FinderService			finderService;


	// Constructors -------------------------------

	public ProcessionService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Procession create() {
		Procession result;

		result = new Procession();
		result.setTicker("000000-XXXXX");
		result.setFloats(Collections.<Float> emptySet());

		return result;
	}
	public Procession save(final Procession procession, final int rowLimit, final int columnLimit) {
		Assert.notNull(procession);

		final Procession result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getArea() != null);

		if (procession.getId() == 0) {
			procession.setTicker(this.utilityService.generateValidTicker(procession.getMoment()));

			final Integer[][] matrizProcession;

			matrizProcession = new Integer[rowLimit][columnLimit];

			for (int i = 0; i < matrizProcession.length; i++)
				for (int j = 0; j < matrizProcession[0].length; j++)
					matrizProcession[i][j] = 0;
			procession.setMatrizProcession(matrizProcession);
		}

		result = this.processionRepository.save(procession);

		return result;
	}
	public void delete(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(this.processionRepository.exists(procession.getId()));

		Brotherhood brotherhood;
		Collection<Request> requests;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.getBrotherhoodToProcession(procession).equals(brotherhood));

		requests = this.requestService.findRequestByProcession(procession.getId());

		for (final Request r : requests)
			this.requestService.deleteRequest(r);

		this.finderService.removeProcessionToFinder(procession);

		this.processionRepository.delete(procession);

	}
	public Procession findOne(final int processionId) {
		Procession result;

		result = this.processionRepository.findOne(processionId);

		return result;
	}

	public Collection<Procession> findAll() {
		Collection<Procession> results;

		results = this.processionRepository.findAll();

		return results;
	}
	public Procession findOneToEdit(final int processionId) {
		Procession result;
		Brotherhood brotherhood;

		result = this.processionRepository.findOne(processionId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.getBrotherhoodToProcession(result).equals(brotherhood));

		return result;
	}

	public Procession findOneToDisplay(final int processionId) {
		Procession result;

		result = this.processionRepository.findOne(processionId);

		Assert.notNull(result);
		Assert.isTrue(result.getIsFinalMode() == true);

		return result;
	}

	public Procession addToMatriz(final Procession procession, final Integer rowProcession, final Integer columnProcession) {
		Assert.notNull(procession);

		Procession result;
		Integer[][] matriz;

		matriz = procession.getMatrizProcession();
		matriz[rowProcession - 1][columnProcession - 1] = 1;
		procession.setMatrizProcession(matriz);

		result = this.processionRepository.save(procession);

		return result;
	}

	public Procession removeToMatriz(final Procession procession, final Integer rowProcession, final Integer columnProcession) {
		Assert.notNull(procession);

		Procession result;
		Integer[][] matriz;

		matriz = procession.getMatrizProcession();
		matriz[rowProcession - 1][columnProcession - 1] = 0;
		procession.setMatrizProcession(matriz);

		result = this.processionRepository.save(procession);

		return result;
	}

	// Other business methods ---------------------

	public Double[] findDataNumberResultsPerFinder() {
		Double[] result;

		result = this.processionRepository.findDataNumberResultsPerFinder();
		Assert.notNull(result);

		return result;
	}

	public Collection<Procession> findProcessionLess30days() {
		Date today;
		final Date more30days;
		Collection<Procession> processions;
		Calendar calendar;

		calendar = Calendar.getInstance();
		today = this.utilityService.current_moment();

		calendar.setTime(today);
		calendar.add(Calendar.DAY_OF_YEAR, 30);

		more30days = calendar.getTime();
		processions = this.processionRepository.findProcessionLess30days(today, more30days);

		return processions;

	}

	protected String existTicker(final String ticker) {
		String result;

		result = this.processionRepository.existTicker(ticker);

		return result;
	}
	private Brotherhood getBrotherhoodToProcession(final Procession procession) {
		Brotherhood result;
		Collection<Float> floats;
		Float floatt;

		floats = procession.getFloats();
		floatt = floats.iterator().next();
		result = floatt.getBrotherhood();

		return result;

	}
	public Boolean floatBelongtToProcession(final int floatId) {
		Boolean res;
		Collection<Procession> processions;

		processions = this.processionRepository.floatBelongtToProcession(floatId);
		if (processions.size() == 0)
			res = true;
		else
			res = false;

		return res;

	}
	public Collection<Procession> findProcessionByBrotherhood(final int id) {
		Collection<Procession> processions;

		processions = this.processionRepository.findProcessionByBrotherhood(id);

		return processions;
	}

	public Collection<Procession> findProcessionFinalByBrotherhood(final int id) {
		Collection<Procession> processions;

		processions = this.processionRepository.findProcessionFinalByBrotherhood(id);

		return processions;
	}
	public void makeFinal(final Procession procession) {
		Brotherhood principal;
		Brotherhood owner;

		principal = this.brotherhoodService.findByPrincipal();
		owner = this.getBrotherhoodToProcession(procession);

		Assert.isTrue(owner.equals(principal));

		procession.setIsFinalMode(true);
	}

}
