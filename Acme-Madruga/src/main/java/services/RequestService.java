
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Actor;
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
		this.checkByPrincipal(result);

		return result;
	}
	//
	//	public Request create(final Procession procession) {
	//		//No se puede crear si ya existe una con estemiembro y procession
	//		this.checkNoExistRequestMemberProcession(procession);
	//		//TODO: En vistas restrinjo que solo pueda crearlo un member
	//		//asegurate que 
	//		final che
	//		Request result;
	//		final Actor principal;
	//
	//		principal = this.actorService.findPrincipal();
	//
	//		result = new Request();
	//		result.setActor(principal);
	//		result.setMessages(Collections.<Message> emptySet());
	//
	//		return result;
	//	}
	//
	//	public Request save(final Request request) {
	//		Assert.notNull(request);
	//		Assert.isTrue(!request.getIsSystemRequest());
	//		this.checkByPrincipal(request);
	//		this.checkName(request);
	//
	//		Request result;
	//
	//		result = this.requestRepository.save(request);
	//
	//		return result;
	//	}
	//
	//	public void delete(final Request request) {
	//		Assert.notNull(request);
	//		Assert.isTrue(!request.getIsSystemRequest());
	//		Assert.isTrue(this.requestRepository.exists(request.getId()));
	//
	//		// ¿Que ocurre si borro una carpeta que contiene mensajes?
	//
	//		// ¿Que ocurre si borro una carpeta que tiene carpetas dentro?
	//
	//	}

	// Other business methods ---------------------

	// Private methods ---------------------------

	private void checkByPrincipal(final Request request) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		//Assert.isTrue(request.getActor().equals(principal));
	}

}
