
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Request;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository ---------------------------------

	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	// Other supporting services --------------------------

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private Validator			validator;


	// Constructors ---------------------------------------

	public EnrolmentService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	protected Enrolment create(final int brotherhoodId) {
		Enrolment result;
		Brotherhood brotherhood;
		Member member;

		result = new Enrolment();
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		member = this.memberService.findByPrincipal();

		result.setMember(member);
		result.setBrotherhood(brotherhood);

		return result;
	}

	private Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		this.checkOwnerBrotherhood(enrolment);

		Enrolment saved;

		saved = this.enrolmentRepository.save(enrolment);

		return saved;
	}

	private void delete(final Enrolment enrolment) {
		Collection<Request> requests;

		requests = this.requestService.findRequestByMemberId(enrolment.getMember().getId());
		for (final Request r : requests)
			this.requestService.deleteDropOut(r);

		this.enrolmentRepository.delete(enrolment);
	}

	public Enrolment findOne(final int enrolmentId) {
		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);

		return result;
	}

	public Enrolment findOneToEdit(final int enrolmentId) {
		Enrolment result;

		result = this.findOne(enrolmentId);
		this.checkOwnerBrotherhood(result);

		return result;
	}

	// Other business methods -----------------------------

	public void reject(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		this.checkOwnerBrotherhood(enrolment);
		this.checkIsRequest(enrolment);

		this.enrolmentRepository.delete(enrolment);
	}

	public void remove(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		this.checkOwnerBrotherhood(enrolment);

		this.delete(enrolment);
	}

	public void dropOut(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		this.checkOwnerMember(enrolment);

		this.delete(enrolment);
	}

	public Enrolment saveToEditPosition(final Enrolment enrolment) {
		Enrolment saved;

		this.checkIsActive(enrolment);

		saved = this.save(enrolment);

		return saved;
	}

	public Enrolment enrol(final Enrolment enrolment) {
		Enrolment saved;

		this.checkIsRequest(enrolment);

		enrolment.setRegisteredMoment(this.utilityService.current_moment());
		saved = this.save(enrolment);

		return saved;
	}

	public void requestEnrolment(final int brotherhoodId) {
		Enrolment enrolment;

		enrolment = this.create(brotherhoodId);
		this.enrolmentRepository.save(enrolment);
	}

	@Transactional(propagation = Propagation.NEVER)
	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result;

		if (enrolment.getId() == 0)
			result = enrolment;
		else {
			result = this.enrolmentRepository.findOne(enrolment.getId());

			result.setPosition(enrolment.getPosition());

			this.validator.validate(result, binding);
		}

		return result;
	}

	public boolean findIsEnrolledIn(final int memberId, final int brotherhoodId) {
		boolean result;

		result = this.enrolmentRepository.findActiveEnrolment(memberId, brotherhoodId) != null;

		return result;
	}

	public boolean findExistEnrolmentRequestOf(final int memberId, final int brotherhoodId) {
		boolean result;

		result = this.enrolmentRepository.findRequestEnrolment(memberId, brotherhoodId) != null;

		return result;
	}

	public Enrolment findByBrotherhoodId(final int brotherhoodId) {
		Enrolment result;
		Member principal;

		principal = this.memberService.findByPrincipal();
		result = this.enrolmentRepository.findActiveEnrolment(principal.getId(), brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Enrolment> findEnroledMembers(final int brotherhoodId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findEnroledMembers(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Enrolment> findRequestEnrolments() {
		Collection<Enrolment> result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		result = this.enrolmentRepository.findRequestEnrolments(brotherhood.getId());
		Assert.notNull(result);

		return result;
	}

	private void checkOwnerBrotherhood(final Enrolment enrolment) {
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(brotherhood.getId() == enrolment.getBrotherhood().getId());
	}

	private void checkOwnerMember(final Enrolment enrolment) {
		Member member;

		member = this.memberService.findByPrincipal();
		Assert.isTrue(member.getId() == enrolment.getMember().getId());
	}

	private void checkIsRequest(final Enrolment enrolment) {
		Assert.isTrue((enrolment.getDropOutMoment() == null) && (enrolment.getRegisteredMoment() == null));
	}

	private void checkIsActive(final Enrolment enrolment) {
		Assert.isTrue((enrolment.getDropOutMoment() == null) && (enrolment.getRegisteredMoment() != null));
	}

}
