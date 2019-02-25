
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;
import forms.SocialProfileForm;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


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

		result = this.socialProfileRepository.save(socialProfile);

		return result;
	}

	public void delete(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		this.checkByPrincipal(socialProfile);

		this.socialProfileRepository.delete(socialProfile);
	}

	public SocialProfile reconstruct(final SocialProfileForm socialProfileForm, final BindingResult binding) {
		final SocialProfile result;

		if (socialProfileForm.getId() == 0) {

			result = this.create();
			result.setNick(socialProfileForm.getNick());
			result.setId(socialProfileForm.getId());
			result.setSocialNetwork(socialProfileForm.getSocialNetwork());
			result.setLinkProfile(socialProfileForm.getLinkProfile());
		} else {
			result = this.findOneToEdit(socialProfileForm.getId());
			result.setNick(socialProfileForm.getNick());
			result.setId(socialProfileForm.getId());
			result.setSocialNetwork(socialProfileForm.getSocialNetwork());
			result.setLinkProfile(socialProfileForm.getLinkProfile());
		}
		this.validator.validate(result, binding);
		return result;
	}

	// Other business methods -------------------------------------------------
	protected void checkByPrincipal(final SocialProfile socialProfile) {
		final Actor actor;

		actor = this.actorService.findPrincipal();

		Assert.isTrue(socialProfile.getActor().equals(actor));
	}

	public Collection<SocialProfile> findSocialProfilesByActor(final int actorId) {
		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findSocialProfilesByActor(actorId);

		return result;
	}

	public String validateNick(final SocialProfileForm socialProfileForm, final BindingResult binding) {
		String result;

		result = socialProfileForm.getNick();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("nick", "socialProfile.error.blank", "Must not be blank");

		return result;
	}

	public String validateSocialNetwork(final SocialProfileForm socialProfileForm, final BindingResult binding) {
		String result;

		result = socialProfileForm.getLinkProfile();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("socialNetwork", "socialProfile.error.blank", "Must not be blank");

		return result;
	}

	public String validateLinkProfile(final SocialProfileForm socialProfileForm, final BindingResult binding) {
		String result;

		result = socialProfileForm.getLinkProfile();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("linkProfile", "socialProfile.error.blank", "Must not be blank");

		return result;
	}

	public String validateLinkProfileUnique(final SocialProfileForm socialProfileForm, final BindingResult binding) {
		String result;
		Collection<SocialProfile> socialProfiles;

		result = socialProfileForm.getLinkProfile();
		if (result != null) {
			socialProfiles = this.socialProfileRepository.findLinkSocialProfile(socialProfileForm.getLinkProfile());
			if (!socialProfiles.isEmpty())
				binding.rejectValue("linkProfile", "socialProfile.error.unique", "Already exist an social profile with this URL");
		}

		return result;
	}

}
