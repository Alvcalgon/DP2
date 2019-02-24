
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.FinderRepository;
import domain.Finder;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed repository ---------------------------------

	@Autowired
	private FinderRepository	finderRepository;


	// Other supporting services --------------------------

	// Constructors ---------------------------------------

	public FinderService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	// Other business methods -----------------------------

	public Double findRatioEmptyVsNonEmpty() {
		Double result;

		result = this.finderRepository.findRatioEmptyVsNonEmpty();

		return result;
	}
	public void removeProcessionToFinder(final Procession procession) {
		Collection<Finder> finders;
		Collection<Procession> processions;

		finders = this.finderRepository.findFinderByProcession(procession.getId());

		for (final Finder f : finders) {
			processions = f.getProcessions();
			for (final Procession p : processions)
				if (p.equals(procession))
					this.finderRepository.delete(f);
		}

	}

}
