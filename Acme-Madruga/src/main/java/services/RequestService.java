
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed repository --------------------------
	@Autowired
	private RequestRepository	requestRepository;

	// Other supporting services -------------------
	@Autowired
	private ActorService		actorService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);

		return result;
	}

	public Request findOneToEditOrDisplay(final int requestId) {
		Request result;
		result = this.requestRepository.findOne(requestId);

		Assert.notNull(result);
		this.checkPrincipalIsBrotherhoodOfProcession(requestId);

		return result;
	}

	public Request create(final Procession procession) {
		Assert.notNull(procession);
		this.checkNoExistRequestMemberProcession(procession);
		this.checkPrincipalIsMemberOfBrotherhoodOfProcession(procession);
		Request result;
		Member member;

		member = this.memberService.findByPrincipal();

		result = new Request();
		result.setMember(member);
		result.setProcession(procession);
		result.setStatus("PENDING");

		return result;
	}

	public Request saveNew(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() == 0);
		final Request result;

		result = this.requestRepository.save(request);

		return result;
	}

	public Request saveEditApproved(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(!(request.getId() == 0));
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"));
		Brotherhood brotherhoodProcession;
		brotherhoodProcession = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());
		Assert.isTrue(brotherhoodProcession.getId() == this.brotherhoodService.findByPrincipal().getId());
		Assert.isTrue(request.getStatus().equals("PENDING"));
		final Request result;

		request.setStatus("APPROVED");
		result = this.requestRepository.save(request);

		return result;
	}

	public Request saveEditRejected(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(!(request.getId() == 0));
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"));
		Brotherhood brotherhoodProcession;
		brotherhoodProcession = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());
		Assert.isTrue(brotherhoodProcession.getId() == this.brotherhoodService.findByPrincipal().getId());
		Assert.isTrue(request.getStatus().equals("PENDING"));
		final Request result;

		request.setStatus("REJECTED");
		result = this.requestRepository.save(request);

		return result;
	}

	public void delete(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getStatus().equals("PENDING"));
		this.checkPrincipalIsMemberOfBrotherhoodOfProcession(request.getProcession());

		this.requestRepository.delete(request);
	}

	// Other business methods ---------------------

	// Private methods ---------------------------
	private void checkNoExistRequestMemberProcession(final Procession procession) {
		Member member;
		Collection<Request> requests;

		member = this.memberService.findByPrincipal();
		requests = this.requestRepository.findRequestByMemberProcession(member.getId(), procession.getId());

		Assert.isTrue(requests.isEmpty());
	}

	private void checkPrincipalIsMemberOfBrotherhoodOfProcession(final Procession procession) {
		Member member;
		final Brotherhood brotherhoodMember;
		final Brotherhood brotherhoodProcession;

		member = this.memberService.findByPrincipal();
		//TODO brotherhoodMember = this.memberService.findBrotherhood(member);
		brotherhoodProcession = this.brotherhoodService.findBrotherhoodByProcession(procession.getId());

		//TODO Assert.isTrue(brotherhoodMember.getId() == brotherhoodProcession.getId());
	}

	private void checkPrincipalIsBrotherhoodOfProcession(final int requestId) {
		final Brotherhood brotherhood;

	}

	public Collection<Request> findRequestByMember() {
		Collection<Request> requests;
		Member member;

		member = this.memberService.findByPrincipal();
		requests = this.requestRepository.findRequestByMember(member.getId());

		return requests;

	}

}
