
package services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProcessionRepository;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

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


	// Constructors -------------------------------

	public ProcessionService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Procession create() {
		Procession result;

		result = new Procession();
		result.setTicker(this.utilityService.generateValidTicker());
		result.setFloats(Collections.<Float> emptySet());

		return result;
	}
	public Procession save(final Procession procession) {
		Assert.notNull(procession);
		//TODO:preguntar

		Procession result;

		result = this.processionRepository.save(procession);

		return result;
	}

	public void delete(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(this.processionRepository.exists(procession.getId()));

		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		//TODO: preguntar
		Assert.isTrue(this.getBrotherhoodToProcession(procession).equals(brotherhood));

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
	public Page<Procession> findAllPage(final Pageable pageable) {
		Page<Procession> result;

		result = this.processionRepository.findAll(pageable);

		Assert.notNull(result);

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
		Collection<Procession> floats;

		today = this.utilityService.current_moment();
		more30days = this.utilityService.current_moment();
		floats = this.processionRepository.findProcessionLess30days(today, more30days);

		return floats;

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

}
