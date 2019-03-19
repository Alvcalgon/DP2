
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private MessageService		messageService;

	@Autowired
	private Validator			validator;


	// Constructors ---------------------------------------

	public EnrolmentService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	private Enrolment create(final Brotherhood brotherhood) {
		Enrolment result;
		Member member;

		result = new Enrolment();
		member = this.memberService.findByPrincipal();

		result.setMember(member);
		result.setBrotherhood(brotherhood);

		return result;
	}

	private Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getBrotherhood().getArea() != null);
		this.checkOwnerBrotherhood(enrolment);

		Enrolment saved;

		saved = this.enrolmentRepository.save(enrolment);

		return saved;
	}

	protected void deleteEnrolments(final Member member) {
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentRepository.findByMemberId(member.getId());
		Assert.notNull(enrolments);

		this.enrolmentRepository.deleteInBatch(enrolments);
	}

	protected void deleteEnrolments(final Brotherhood brotherhood) {
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentRepository.findByBrotherhoodId(brotherhood.getId());
		Assert.notNull(enrolments);

		this.enrolmentRepository.deleteInBatch(enrolments);
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
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));
		this.checkOwnerBrotherhood(enrolment);
		this.checkIsRequest(enrolment);

		this.enrolmentRepository.delete(enrolment);
	}

	public void remove(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		this.checkOwnerBrotherhood(enrolment);
		this.checkIsActive(enrolment);

		this.manageExitOfMember(enrolment);
		this.messageService.notificationRemove(enrolment);
	}

	public void dropOut(final int brotherhoodId) {
		Enrolment enrolment;

		enrolment = this.findActiveByBrotherhoodId(brotherhoodId);

		this.checkOwnerMember(enrolment);
		this.manageExitOfMember(enrolment);
		this.messageService.notificationDropOut(enrolment);
	}

	public Enrolment saveToEditPosition(final Enrolment enrolment) {
		Enrolment saved;

		this.checkIsActive(enrolment);

		saved = this.save(enrolment);

		return saved;
	}

	public Enrolment enrol(final Enrolment enrolment) {
		Enrolment saved, inactive;

		this.checkIsRequest(enrolment);

		inactive = this.enrolmentRepository.findInactiveEnrolment(enrolment.getMember().getId(), enrolment.getBrotherhood().getId());
		if (inactive != null)
			this.enrolmentRepository.delete(inactive);

		enrolment.setRegisteredMoment(this.utilityService.current_moment());
		saved = this.save(enrolment);

		this.messageService.notificationEnrolment(saved);

		return saved;
	}

	public void requestEnrolment(final int brotherhoodId) {
		Enrolment enrolment;
		Brotherhood brotherhood;
		Member member;

		member = this.memberService.findByPrincipal();
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		Assert.isTrue(!this.findExistActiveEnrolment(member.getId(), brotherhoodId));
		Assert.isTrue(!this.findExistRequestEnrolment(member.getId(), brotherhoodId));
		Assert.isTrue(brotherhood.getArea() != null);

		enrolment = this.create(brotherhood);
		this.enrolmentRepository.save(enrolment);
	}

	public Collection<Enrolment> findRequestEnrolments() {
		Collection<Enrolment> result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		result = this.enrolmentRepository.findRequestEnrolments(brotherhood.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<Enrolment> findAllEnrolmentsByPrincipal() {
		Collection<Enrolment> result;
		Member member;

		member = this.memberService.findByPrincipal();
		result = this.enrolmentRepository.findAllEnrolmentsByMemberId(member.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<Enrolment> findEnroledMembers(final int brotherhoodId) {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findEnroledMembers(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	// Ancillary methods ----------------------------------

	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result, enrolmentStored;

		result = new Enrolment();
		enrolmentStored = this.enrolmentRepository.findOne(enrolment.getId());

		result.setId(enrolment.getId());
		result.setVersion(enrolmentStored.getVersion());
		result.setBrotherhood(enrolmentStored.getBrotherhood());
		result.setDropOutMoment(enrolmentStored.getDropOutMoment());
		result.setMember(enrolmentStored.getMember());
		result.setRegisteredMoment(enrolmentStored.getRegisteredMoment());
		result.setPosition(enrolment.getPosition());

		this.validator.validate(result, binding);

		return result;
	}

	public boolean findIsEnrolledIn(final int memberId, final int brotherhoodId) {
		boolean result;

		result = this.enrolmentRepository.findActiveEnrolment(memberId, brotherhoodId) != null;

		return result;
	}

	public boolean findExistRequestEnrolment(final int memberId, final int brotherhoodId) {
		boolean result;

		result = this.enrolmentRepository.findRequestEnrolment(memberId, brotherhoodId) != null;

		return result;
	}

	private boolean findExistActiveEnrolment(final int memberId, final int brotherhoodId) {
		boolean result;

		result = this.enrolmentRepository.findActiveEnrolment(memberId, brotherhoodId) != null;

		return result;
	}

	private Enrolment findActiveByBrotherhoodId(final int brotherhoodId) {
		Enrolment result;
		Member principal;

		principal = this.memberService.findByPrincipal();
		result = this.enrolmentRepository.findActiveEnrolment(principal.getId(), brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	private void manageExitOfMember(final Enrolment enrolment) {
		Collection<Request> requests;

		requests = this.requestService.findRequestByMemberIdBrotherhoodId(enrolment.getMember().getId(), enrolment.getBrotherhood().getId());
		for (final Request r : requests)
			this.requestService.deleteDropOut(r);

		enrolment.setPosition(null);
		enrolment.setDropOutMoment(this.utilityService.current_moment());
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
