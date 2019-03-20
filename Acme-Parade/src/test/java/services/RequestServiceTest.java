
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// The SUT ---------------------------
	@Autowired
	private RequestService	requestService;

	@Autowired
	private ParadeService	paradeService;


	//Test ------------------------------------------------
	//
	//	@Test
	//	public void driverCreate() {
	//		final Object testingData[][] = {
	//			/*
	//			 * A: Acme-Madrugá Req.7 Crear una solicitud para el desfile
	//			 * B: Test positivo
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"member1", "parade7", null
	//			},
	//			/*
	//			 * A: Acme-Madrugá Req.7
	//			 * B: Test negativo: Solicitar a un desfile de una hermandad a la que no pertenece el miembro
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"member2", "parade1", IllegalArgumentException.class
	//			},
	//			/*
	//			 * A: Acme-Madrugá Req.7
	//			 * B: Test negativo: Solicitar a un desfile en el que ya tiene una solicitud
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"member1", "parade1", IllegalArgumentException.class
	//			},
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	//
	//	}
	//
	//	protected void templateCreate(final String username, final int paradeId, final Class<?> expected) {
	//		Class<?> caught;
	//		Request request, requestSaved;
	//		Parade parade;
	//
	//		this.startTransaction();
	//
	//		caught = null;
	//		try {
	//			super.authenticate(username);
	//			parade = this.paradeService.findOne(paradeId);
	//
	//			request = this.requestService.create(parade);
	//			requestSaved = this.requestService.saveNew(request);
	//			this.requestService.flush();
	//
	//			Assert.notNull(requestSaved);
	//			Assert.isTrue(requestSaved.getId() != 0);
	//
	//			super.unauthenticate();
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		this.rollbackTransaction();
	//
	//		super.checkExceptions(expected, caught);
	//	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			/*
			 * A: Acme-Madrugá Req.7 Editar una solicitud
			 * B:
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "request1", 3, 3, "APPROVED", null
			},
			/*
			 * A: Acme-Madrugá Req.7 Editar una solicitud
			 * B:Test negativo: Editar una solicitud que es de otra hermandad
			 * C:
			 * D:
			 */
			{
				"brotherhood3", "request1", 3, 3, "PENDING", IllegalArgumentException.class
			},
			/*
			 * A: Acme-Madrugá Req.7 Editar una solicitud
			 * B:Test negativo: Editar una solicitud poniendole una posicion que ya está asignada
			 * C:
			 * D:
			 */
			{
				"brotherhood2", "request6", 2, 1, "APPROVED", IllegalArgumentException.class
			},

			/*
			 * A: Acme-Madrugá Req.7 Editar una solicitud
			 * B:Test negativo: Editar una solicitud con estado aceptado
			 * C:
			 * D:
			 */
			{
				"brotherhood3", "request1", 1, 1, "REJECTED", IllegalArgumentException.class
			},

			/*
			 * A: Acme-Madrugá Req.11.1 Editar una solicitud
			 * B:Test negativo: Un miembro no puede editar
			 * C:
			 * D:
			 */
			{
				"member1", "request1", 1, 1, "REJECTED", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Integer) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}
	protected void templateEdit(final String username, final int requestId, final Integer column, final Integer row, final String status, final Class<?> expected) {
		Class<?> caught;
		Request request;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			request = this.requestService.findOne(requestId);
			request.setColumnParade(column);
			request.setRowParade(row);
			request.setStatus(status);
			this.requestService.saveEdit(request);
			this.requestService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}
	//
	//	@Test
	//	public void driverChangeStatus() {
	//		final Object testingData[][] = {
	//			/*
	//			 * A: Acme-Madrugá Req.7 Cambiar el estado a aceptado
	//			 * B:
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood2", "request01", null, "APPROVED", null
	//			},
	//
	//			/*
	//			 * A: Acme-Madrugá Req.7 Cambiar el estado a rechazado y ponerle motivo de cancelación
	//			 * B:
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood2", "request01", "Es inapropiado", "REJECTED", null
	//			},
	//
	//			/*
	//			 * A: Acme-Madrugá Req.7 Cambiar el estado
	//			 * B: Test negativo: El autenticado no es el dueño de la parade a la que hace referencia la request
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood1", "request01", null, "APPROVED", IllegalArgumentException.class
	//			},
	//
	//			/*
	//			 * A: Acme-Madrugá Req.7 Cambiar el estado
	//			 * B: Test negativo: Cambiar el estado a una request ya aprobada
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood1", "request1", null, "APPROVED", IllegalArgumentException.class
	//			},
	//
	//			/*
	//			 * A: Acme-Madrugá Req.7 Cambiar el estado
	//			 * B: Test negativo: Cambiar el estado a rechazado y dejar en blanco el motivo de cancelación
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood2", "request01", "", "REJECTED", null
	//			},
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateChangeStatus((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	//
	//	}
	//	protected void templateChangeStatus(final String username, final int requestId, final String reasonWhy, final String status, final Class<?> expected) {
	//		Class<?> caught;
	//		Request request;
	//
	//		this.startTransaction();
	//
	//		caught = null;
	//		try {
	//			super.authenticate(username);
	//
	//			request = this.requestService.findOne(requestId);
	//
	//			if (status == "APPROVED")
	//				this.requestService.saveEditApproved(request);
	//			else if (status == "REJECTED") {
	//				request.setReasonWhy(reasonWhy);
	//				this.requestService.saveEditRejected(request);
	//
	//			}
	//			this.requestService.flush();
	//
	//			super.unauthenticate();
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		this.rollbackTransaction();
	//
	//		super.checkExceptions(expected, caught);
	//	}
	//
	//	/*
	//	 * A:Ver el request de otro usuario
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void displayMember_negative_test() {
	//		super.authenticate("member2");
	//
	//		int requestId;
	//
	//		requestId = super.getEntityId("request1");
	//		this.requestService.findOneToMember(requestId);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Ver el request de otro usuario
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test
	//	public void displayMember_positive_test() {
	//		super.authenticate("member1");
	//
	//		int requestId;
	//
	//		requestId = super.getEntityId("request1");
	//		this.requestService.findOneToMember(requestId);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Ver el request de otro usuario
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void displayBrotherhood_negative_test() {
	//		super.authenticate("brotherhood2");
	//
	//		int requestId;
	//
	//		requestId = super.getEntityId("request1");
	//		this.requestService.findOneToBrotherhood(requestId);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Ver el request de otro usuario
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test
	//	public void displayBrotherhood_positive_test() {
	//		super.authenticate("brotherhood1");
	//
	//		int requestId;
	//
	//		requestId = super.getEntityId("request1");
	//		this.requestService.findOneToBrotherhood(requestId);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Borrar
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test
	//	public void delete_positive_test() {
	//		super.authenticate("member1");
	//
	//		int requestId;
	//		Request request;
	//
	//		requestId = super.getEntityId("request01");
	//		request = this.requestService.findOne(requestId);
	//
	//		this.requestService.deleteCancel(request);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Borrar con estado a aproved
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void delete_negativo_test() {
	//		super.authenticate("member1");
	//
	//		int requestId;
	//		Request request;
	//
	//		requestId = super.getEntityId("request1");
	//		request = this.requestService.findOne(requestId);
	//
	//		this.requestService.deleteCancel(request);
	//
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * A:Borrar uno que no es suyo
	//	 * B:
	//	 * C:
	//	 * D:
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void delete_negative1_test() {
	//		super.authenticate("member3");
	//
	//		int requestId;
	//		Request request;
	//
	//		requestId = super.getEntityId("request01");
	//		request = this.requestService.findOne(requestId);
	//
	//		this.requestService.deleteCancel(request);
	//
	//		super.unauthenticate();
	//	}
}
