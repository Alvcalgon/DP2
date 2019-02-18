
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Box;

@Service
@Transactional
public class BoxService {

	// Managed repository --------------------------
	@Autowired
	private BoxRepository	boxRepository;


	// Other supporting services -------------------

	// Constructors -------------------------------
	public BoxService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Box findOne(final int boxId) {
		Box result;

		result = this.boxRepository.findOne(boxId);

		return result;
	}

	public Box findOneToEditOrDisplay(final int boxId) {
		Box result;

		result = this.boxRepository.findOne(boxId);

		Assert.notNull(result);
		//TODO:this.actorService.findByPrincipal();

		return result;
	}

	// Other business methods ---------------------

	// Protected methods --------------------------

	// Private methods ---------------------------

}
