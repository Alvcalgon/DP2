
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

	public Request findOneToBrotherhood(final int requestId) {
		Request result;
		result = this.requestRepository.findOne(requestId);

		Assert.notNull(result);
		this.checkPrincipalIsBrotherhoodOfProcession(requestId);

		return result;
	}

	public Request findOneToMember(final int requestId) {
		Request result;
		result = this.requestRepository.findOne(requestId);

		Assert.notNull(result);
		this.checkPrincipalIsMemberOfBrotherhoodOfProcession(result.getProcession());
		this.checkPrincipalIsMemberOfRequest(result);

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

	public Request saveEdit(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		this.checkPrincipalIsBrotherhoodOfRequest(request);
		final Request result;

		result = this.requestRepository.save(request);

		return result;
	}

	public Request saveEditApproved(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(!(request.getId() == 0));
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"));
		this.checkPrincipalIsBrotherhoodOfRequest(request);
		Assert.isTrue(request.getStatus().equals("PENDING"));
		final Request result;
		Integer[][] matrizProcession;

		matrizProcession = request.getProcession().getMatrizProcession();
		for (int i = 0; i < matrizProcession.length; i++) {
			for (int j = 0; j < matrizProcession[0].length; j++)
				if (matrizProcession[i][j].equals(0)) {
					request.setRowProcession(i + 1);
					request.setColumnProcession(j + 1);
					break;
				}
			break;
		}
		request.setStatus("APPROVED");
		result = this.requestRepository.save(request);

		return result;
	}
	public Request saveEditRejected(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(!(request.getId() == 0));
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"));
		this.checkPrincipalIsBrotherhoodOfRequest(request);
		Assert.isTrue(request.getStatus().equals("PENDING"));
		final Request result;

		request.setStatus("REJECTED");
		result = this.requestRepository.save(request);

		return result;
	}

	public void deleteCancel(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getStatus().equals("PENDING"));
		this.checkPrincipalIsMemberOfBrotherhoodOfProcession(request.getProcession());
		this.checkPrincipalIsMemberOfRequest(request);

		this.requestRepository.delete(request);
	}

	public void deleteRequest(final Request request) {
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
		final Collection<Brotherhood> brotherhoodsMember;
		final Brotherhood brotherhoodProcession;

		member = this.memberService.findByPrincipal();
		brotherhoodsMember = this.brotherhoodService.findByMemberId(member.getId());
		brotherhoodProcession = this.brotherhoodService.findBrotherhoodByProcession(procession.getId());

		Assert.isTrue(brotherhoodsMember.contains(brotherhoodProcession));
	}

	private void checkPrincipalIsBrotherhoodOfProcession(final int requestId) {
		Brotherhood brotherhoodProcession;
		Brotherhood brotherhoodAuthenticate;
		Request request;

		request = this.requestRepository.findOne(requestId);
		brotherhoodProcession = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());
		brotherhoodAuthenticate = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(brotherhoodProcession.getId() == brotherhoodAuthenticate.getId());

	}

	private void checkPrincipalIsMemberOfRequest(final Request request) {
		Member memberRequest;
		Member memberPrincipal;

		memberRequest = request.getMember();
		memberPrincipal = this.memberService.findByPrincipal();

		Assert.isTrue(memberRequest.equals(memberPrincipal));
	}

	private void checkPrincipalIsBrotherhoodOfRequest(final Request request) {
		Brotherhood brotherhoodRequest;
		Brotherhood brotherhoodPrincipal;

		brotherhoodRequest = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());
		brotherhoodPrincipal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(brotherhoodRequest.equals(brotherhoodPrincipal));
	}

	public Collection<Request> findPendingRequestByMember() {
		Collection<Request> requests;
		Member member;

		member = this.memberService.findByPrincipal();
		requests = this.requestRepository.findPendingRequestByMember(member.getId());

		return requests;

	}

	public Collection<Request> findApprovedRequestByMember() {
		Collection<Request> requests;
		Member member;

		member = this.memberService.findByPrincipal();
		requests = this.requestRepository.findApprovedRequestByMember(member.getId());

		return requests;

	}

	public Collection<Request> findRejectedRequestByMember() {
		Collection<Request> requests;
		Member member;

		member = this.memberService.findByPrincipal();
		requests = this.requestRepository.findRejectedRequestByMember(member.getId());

		return requests;

	}

	public Collection<Request> findPendingRequestByBrotherhood() {
		Collection<Request> requests;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		requests = this.requestRepository.findPendingRequestByBrotherhood(brotherhood.getId());

		return requests;

	}

	public Collection<Request> findApprovedRequestByBrotherhood() {
		Collection<Request> requests;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		requests = this.requestRepository.findApprovedRequestByBrotherhood(brotherhood.getId());

		return requests;

	}

	public Collection<Request> findRejectedRequestByBrotherhood() {
		Collection<Request> requests;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();
		requests = this.requestRepository.findRejectedRequestByBrotherhood(brotherhood.getId());

		return requests;

	}

	public Collection<Request> findRequestByProcession(final int processionId) {
		Collection<Request> requests;

		requests = this.requestRepository.findRequestByProcession(processionId);

		return requests;

	}

}
