
package services;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;
import domain.Request;
import forms.RequestForm;

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

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private Validator			validator;


	// Constructors -------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Collection<Request> findAll() {
		Collection<Request> results;

		results = this.requestRepository.findAll();

		return results;
	}

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
		//		if (request.getStatus().equals("APPROVED"))
		//			this.checkPositionIsFree(request);
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
		compare: for (int i = 0; i < matrizProcession.length; i++)
			for (int j = 0; j < matrizProcession[0].length; j++)
				if (matrizProcession[i][j] == 0) {
					request.setRowProcession(i + 1);
					request.setColumnProcession(j + 1);
					break compare;
				}
		this.processionService.addToMatriz(request.getProcession(), request.getRowProcession(), request.getColumnProcession());
		request.setStatus("APPROVED");
		result = this.requestRepository.save(request);

		return result;
	}
	public Request saveEditRejected(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(!(request.getId() == 0));
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"));
		this.checkPrincipalIsBrotherhoodOfRequest(request);
		Assert.isTrue(!(request.getStatus().equals("APPROVED")));
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

	public void deleteDropOut(final Request request) {
		if (request.getStatus().equals("APPROVED"))
			this.processionService.removeToMatriz(request.getProcession(), request.getRowProcession(), request.getColumnProcession());
		this.requestRepository.delete(request);
	}

	public RequestForm createRequestForm(final Request request) {
		RequestForm result;

		result = new RequestForm();
		result.setColumnProcession(request.getColumnProcession());
		result.setId(request.getId());
		result.setRowProcession(request.getRowProcession());
		result.setReasonWhy(request.getReasonWhy());
		result.setStatus(request.getStatus());
		result.setProcession(request.getProcession());
		result.setMember(request.getMember());

		return result;
	}

	public Request reconstruct(final RequestForm requestForm, final BindingResult binding) {
		final Request result, requestStored;
		Integer position;
		SortedMap<Integer, List<Integer>> positionsMap;
		Integer row = null, column = null;

		result = new Request();
		requestStored = this.findOneToBrotherhood(requestForm.getId());
		position = requestForm.getPositionProcession();
		positionsMap = this.processionService.positionsFree(requestForm.getProcession());
		row = positionsMap.get(position).get(0);
		column = positionsMap.get(position).get(1);

		this.processionService.addToMatriz(requestStored.getProcession(), row, column);
		this.processionService.removeToMatriz(requestStored.getProcession(), requestStored.getRowProcession(), requestStored.getColumnProcession());

		result.setRowProcession(row);
		result.setColumnProcession(column);
		result.setReasonWhy(requestForm.getReasonWhy());

		result.setStatus(requestStored.getStatus());
		result.setProcession(requestStored.getProcession());
		result.setId(requestStored.getId());
		result.setMember(requestStored.getMember());
		result.setVersion(requestStored.getVersion());

		//this.validator.validate(result, binding);
		return result;
	}

	public Request reconstructReject(final RequestForm requestForm, final BindingResult binding) {
		final Request result, requestStored;

		result = new Request();
		requestStored = this.findOneToBrotherhood(requestForm.getId());

		result.setRowProcession(requestStored.getRowProcession());
		result.setColumnProcession(requestStored.getColumnProcession());
		result.setReasonWhy(requestForm.getReasonWhy());

		result.setStatus(requestStored.getStatus());
		result.setProcession(requestStored.getProcession());
		result.setId(requestStored.getId());
		result.setMember(requestStored.getMember());
		result.setVersion(requestStored.getVersion());

		//this.validator.validate(result, binding);
		return result;
	}
	// Other business methods ---------------------

	// Private methods ---------------------------
	private void checkNoExistRequestMemberProcession(final Procession procession) {
		Member member;
		Collection<Request> requests;

		member = this.memberService.findByPrincipal();
		requests = this.findRequestMemberProcession(member.getId(), procession.getId());

		Assert.isTrue(requests.isEmpty());
	}

	public Collection<Request> findRequestMemberProcession(final int memberId, final int processionId) {
		Collection<Request> requests;

		requests = this.requestRepository.findRequestByMemberProcession(memberId, processionId);

		return requests;
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

	private void checkPositionIsFree(final Request request) {

		Integer row, column;
		Integer[][] matrizProcession;

		row = request.getRowProcession() - 1;
		column = request.getColumnProcession() - 1;
		matrizProcession = request.getProcession().getMatrizProcession();

		Assert.isTrue(matrizProcession[row][column].equals(0));
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

	public Collection<Request> findRequestByMemberId(final int memberId) {
		Collection<Request> result;

		result = this.requestRepository.findRequestByMemberId(memberId);
		Assert.notNull(result);

		return result;
	}

	public String validateReasonWhy(final RequestForm requestForm, final BindingResult binding) {
		String result;

		result = requestForm.getReasonWhy();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("reasonWhy", "request.error.blank", "Must not be blank");

		return result;
	}

	//Queries

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('PENDING'))
	public Double findRatioPendingRequestsProcession(final int processionId) {
		Double result;

		result = this.requestRepository.findRatioPendingRequestsProcession(processionId);

		return result;
	}

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('APPROVED'))
	public Double findRatioAprovedRequestsProcession(final int processionId) {
		Double result;

		result = this.requestRepository.findRatioPendingRequestsProcession(processionId);

		return result;
	}

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('REJECTED'))
	public Double findRatioRejectedRequetsProcession(final int processionId) {
		Double result;

		result = this.requestRepository.findRatioPendingRequestsProcession(processionId);

		return result;
	}

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('PENDING'))
	public Double findRatioPendingRequests() {
		Double result;

		result = this.requestRepository.findRatioPendingRequests();

		return result;
	}

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('APPROVED'))
	public Double findRatioAprovedRequests() {
		Double result;

		result = this.requestRepository.findRatioAprovedRequests();

		return result;
	}

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('REJECTED'))
	public Double findRatioRejectedRequets() {
		Double result;

		result = this.requestRepository.findRatioRejectedRequets();

		return result;
	}

}
