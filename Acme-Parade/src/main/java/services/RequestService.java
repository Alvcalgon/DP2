
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import domain.Parade;
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
	private ParadeService		paradeService;

	@Autowired
	private Validator			validator;

	@Autowired
	private MessageService		messageService;


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
		this.checkPrincipalIsBrotherhoodOfParade(requestId);

		return result;
	}

	public Request findOneToMember(final int requestId) {
		Request result;
		result = this.requestRepository.findOne(requestId);

		Assert.notNull(result);
		this.checkPrincipalIsMemberOfBrotherhoodOfParade(result.getParade());
		this.checkPrincipalIsMemberOfRequest(result);

		return result;
	}

	public Request findOneDeleteToMember(final int requestId) {
		Request result;
		result = this.requestRepository.findOne(requestId);

		Assert.notNull(result);
		Assert.isTrue(result.getStatus().equals("PENDING"));
		this.checkPrincipalIsMemberOfBrotherhoodOfParade(result.getParade());
		this.checkPrincipalIsMemberOfRequest(result);

		return result;
	}

	public Request create(final Parade parade) {
		Assert.notNull(parade);
		this.checkNoExistRequestMemberParade(parade);
		this.checkPrincipalIsMemberOfBrotherhoodOfParade(parade);
		Request result;
		Member member;

		member = this.memberService.findByPrincipal();

		result = new Request();
		result.setMember(member);
		result.setParade(parade);
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
		Integer[][] matrizParade;

		matrizParade = request.getParade().getMatrizParade();
		compare: for (int i = 0; i < matrizParade.length; i++)
			for (int j = 0; j < matrizParade[0].length; j++)
				if (matrizParade[i][j] == 0) {
					request.setRowParade(i + 1);
					request.setColumnParade(j + 1);
					break compare;
				}
		this.paradeService.addToMatriz(request.getParade(), request.getRowParade(), request.getColumnParade());
		request.setStatus("APPROVED");
		result = this.requestRepository.save(request);
		this.messageService.notificationChangeStatus(result);

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
		this.messageService.notificationChangeStatus(result);

		return result;
	}

	public void deleteCancel(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);
		Assert.isTrue(request.getStatus().equals("PENDING"));
		this.checkPrincipalIsMemberOfBrotherhoodOfParade(request.getParade());
		this.checkPrincipalIsMemberOfRequest(request);

		this.requestRepository.delete(request);
	}

	public void deleteRequest(final Request request) {
		this.requestRepository.delete(request);
	}

	public void deleteDropOut(final Request request) {
		if (request.getStatus().equals("APPROVED"))
			this.paradeService.removeToMatriz(request.getParade(), request.getRowParade(), request.getColumnParade());
		this.requestRepository.delete(request);
	}

	public RequestForm createRequestForm(final Request request) {
		RequestForm result;

		result = new RequestForm();
		result.setColumnParade(request.getColumnParade());
		result.setId(request.getId());
		result.setRowParade(request.getRowParade());
		result.setReasonWhy(request.getReasonWhy());
		result.setStatus(request.getStatus());
		result.setParade(request.getParade());
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
		position = requestForm.getPositionParade();
		positionsMap = this.paradeService.positionsFree(requestForm.getParade());
		row = positionsMap.get(position).get(0);
		column = positionsMap.get(position).get(1);

		this.paradeService.addToMatriz(requestStored.getParade(), row, column);
		this.paradeService.removeToMatriz(requestStored.getParade(), requestStored.getRowParade(), requestStored.getColumnParade());

		result.setRowParade(row);
		result.setColumnParade(column);
		result.setReasonWhy(requestForm.getReasonWhy());

		result.setStatus(requestStored.getStatus());
		result.setParade(requestStored.getParade());
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

		result.setRowParade(requestStored.getRowParade());
		result.setColumnParade(requestStored.getColumnParade());
		result.setReasonWhy(requestForm.getReasonWhy().trim());

		result.setStatus(requestStored.getStatus());
		result.setParade(requestStored.getParade());
		result.setId(requestStored.getId());
		result.setMember(requestStored.getMember());
		result.setVersion(requestStored.getVersion());

		this.validator.validate(result, binding);
		return result;
	}
	// Other business methods ---------------------
	public Map<String, List<Double>> findRatioRequestByParade() {
		Map<String, List<Double>> result;
		Collection<Parade> publishedParades;
		Double pendingRatio, approvedRatio, rejectedRatio;

		result = new HashMap<String, List<Double>>();
		publishedParades = this.paradeService.findPublishedParade();

		for (final Parade p : publishedParades) {
			final List<Double> ld = new ArrayList<>();

			pendingRatio = this.findRatioPendingRequestsParade(p.getId());
			approvedRatio = this.findRatioAprovedRequestsParade(p.getId());
			rejectedRatio = this.findRatioRejectedRequetsParade(p.getId());

			ld.add(pendingRatio);
			ld.add(approvedRatio);
			ld.add(rejectedRatio);

			result.put(p.getTicker(), ld);
		}

		return result;
	}

	// Private methods ---------------------------
	private void checkNoExistRequestMemberParade(final Parade parade) {
		Member member;
		Collection<Request> requests;

		member = this.memberService.findByPrincipal();
		requests = this.findRequestMemberParade(member.getId(), parade.getId());

		Assert.isTrue(requests.isEmpty());
	}

	public Collection<Request> findRequestMemberParade(final int memberId, final int paradeId) {
		Collection<Request> requests;

		requests = this.requestRepository.findRequestByMemberParade(memberId, paradeId);

		return requests;
	}

	private void checkPrincipalIsMemberOfBrotherhoodOfParade(final Parade parade) {
		Member member;
		final Collection<Brotherhood> brotherhoodsMember;
		final Brotherhood brotherhoodParade;

		member = this.memberService.findByPrincipal();
		brotherhoodsMember = this.brotherhoodService.findByMemberId(member.getId());
		brotherhoodParade = this.brotherhoodService.findBrotherhoodByParade(parade.getId());

		Assert.isTrue(brotherhoodsMember.contains(brotherhoodParade));
	}

	private void checkPrincipalIsBrotherhoodOfParade(final int requestId) {
		Brotherhood brotherhoodParade;
		Brotherhood brotherhoodAuthenticate;
		Request request;

		request = this.requestRepository.findOne(requestId);
		brotherhoodParade = this.brotherhoodService.findBrotherhoodByParade(request.getParade().getId());
		brotherhoodAuthenticate = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(brotherhoodParade.getId() == brotherhoodAuthenticate.getId());

	}

	private void checkPrincipalIsBrotherhoodOfRequest(final Request request) {
		Brotherhood brotherhoodRequest;
		Brotherhood brotherhoodPrincipal;

		brotherhoodRequest = this.brotherhoodService.findBrotherhoodByParade(request.getParade().getId());
		brotherhoodPrincipal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(brotherhoodRequest.equals(brotherhoodPrincipal));
	}

	private void checkPrincipalIsMemberOfRequest(final Request request) {
		Member memberRequest;
		Member memberPrincipal;

		memberRequest = request.getMember();
		memberPrincipal = this.memberService.findByPrincipal();

		Assert.isTrue(memberRequest.equals(memberPrincipal));
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

	public Collection<Request> findRequestByParade(final int paradeId) {
		Collection<Request> requests;

		requests = this.requestRepository.findRequestByParade(paradeId);

		return requests;
	}

	public Collection<Request> findRequestByMemberId(final int memberId) {
		Collection<Request> result;

		result = this.requestRepository.findRequestByMemberId(memberId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findRequestByMemberIdBrotherhoodId(final int memberId, final int brotherhoodId) {
		Collection<Request> result;

		result = this.requestRepository.findRequestByMemberIdBrotherhoodId(memberId, brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public String validateReasonWhy(final RequestForm requestForm, final BindingResult binding) {
		String result;

		result = requestForm.getReasonWhy().trim();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("reasonWhy", "request.error.blank", "Must not be blank");

		return result;
	}

	//Queries

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('PENDING'))
	public Double findRatioPendingRequestsParade(final int paradeId) {
		Double result;

		result = this.requestRepository.findRatioPendingRequestsParade(paradeId);

		return result;
	}

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('APPROVED'))
	public Double findRatioAprovedRequestsParade(final int paradeId) {
		Double result;

		result = this.requestRepository.findRatioAprovedRequestsParade(paradeId);

		return result;
	}

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('REJECTED'))
	public Double findRatioRejectedRequetsParade(final int paradeId) {
		Double result;

		result = this.requestRepository.findRatioRejectedRequetsParade(paradeId);

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

	protected void flush() {
		this.requestRepository.flush();
	}
}
