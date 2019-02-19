
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProcessionRepository;
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


	// Constructors -------------------------------

	public ProcessionService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Procession create() {
		Procession result;

		result = new Procession();

		return result;
	}

	public Procession save(final Procession procession) {
		Assert.notNull(procession);

		Procession result;

		result = this.processionRepository.save(procession);

		return result;
	}

	public void delete(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(this.processionRepository.exists(procession.getId()));

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

	// Other business methods ---------------------

	public Collection<Procession> findProcessionLess30days() {
		Date today;
		final Date more30days;
		Collection<Procession> floats;

		today = this.utilityService.current_moment();

		floats = this.processionRepository.findProcessionLess30days(today, more30days);

		return floats;

	}

}
