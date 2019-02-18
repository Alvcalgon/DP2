
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository --------------------------

	@Autowired
	private ActorRepository	actorRepository;


	// Other supporting services -------------------

	// Constructors -------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods ------------------------

	// Other business methods ---------------------

	protected Actor findPrincipal() {
		Actor result;
		int userAccountId;

		userAccountId = LoginService.getPrincipal().getId();

		result = this.findActorByUserAccount(userAccountId);
		Assert.notNull(result);

		return result;
	}

	private Actor findActorByUserAccount(final int id) {
		Actor result;

		result = this.actorRepository.findActorByUserAccount(id);

		return result;
	}

}
