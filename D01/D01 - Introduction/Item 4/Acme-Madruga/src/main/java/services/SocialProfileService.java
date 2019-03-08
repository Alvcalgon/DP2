
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public SocialProfileService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public SocialProfile create() {
		SocialProfile result;
		final Actor actor;

		actor = this.actorService.findPrincipal();

		result = new SocialProfile();
		result.setActor(actor);

		return result;
	}

	public SocialProfile findOne(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);

		SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	public SocialProfile findOneDisplay(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);
		SocialProfile result;
		Actor actor;

		actor = this.socialProfileRepository.findOne(socialProfileId).getActor();

		if (actor.getUserAccount().getAuthorities().toString().equals("[ADMIN]")) {
			final Actor principal;
			principal = this.actorService.findPrincipal();
			Assert.isTrue(principal.getId() == actor.getId());
		}

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}
	public SocialProfile findOneToEdit(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);
		SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);

		Assert.notNull(result);
		this.checkByPrincipal(result);

		return result;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findAll();

		return result;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		this.checkByPrincipal(socialProfile);

		SocialProfile result;

		socialProfile.setLinkProfile(socialProfile.getLinkProfile().trim());
		result = this.socialProfileRepository.save(socialProfile);

		return result;
	}

	public void delete(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		this.checkByPrincipal(socialProfile);

		this.socialProfileRepository.delete(socialProfile);
	}

	// Other business methods -------------------------------------------------
	protected void checkByPrincipal(final SocialProfile socialProfile) {
		final Actor actor;

		actor = this.actorService.findPrincipal();

		Assert.isTrue(socialProfile.getActor().equals(actor));
	}

	public Collection<SocialProfile> findSocialProfilesByActor(final int actorId) {
		Collection<SocialProfile> result;

		Actor actor;

		actor = this.actorService.findOne(actorId);

		if (actor.getUserAccount().getAuthorities().toString().equals("[ADMIN]")) {
			final Actor principal;
			principal = this.actorService.findPrincipal();
			Assert.isTrue(principal.getId() == actor.getId());
		}
		result = this.socialProfileRepository.findSocialProfilesByActor(actorId);

		return result;
	}

}
