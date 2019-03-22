
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Parade;
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

	@Test
	public void driverCreate() {
		final Object testingData[][] = {
			/*
			 * A: Acme-Madrugá Req.7,11.1 Crear una solicitud
			 * B: Test positivo
			 * C: 100%. 65/65 Recorre 65 de las 65 líneas de código totales
			 * D: Intencionadamente en blanco. No se comprueban datos
			 */
			{
				"member12", "parade1", null
			},
			/*
			 * A: Acme-Madrugá Req.7,11.1 Crear una solicitud
			 * B: Test negativo: Solicitar a un desfile de una hermandad a la que no pertenece el miembro
			 * C: 68%. 44/65 Recorre 44 de las 65 líneas totales
			 * D:Intencionadamente en blanco. No se comprueban datos
			 */
			{
				"member2", "parade1", IllegalArgumentException.class
			},
			/*
			 * A: Acme-Madrugá Req.7,11.1 Crear una solicitud
			 * B: Test negativo: Solicitar a un desfile en el que ya tiene una solicitud
			 * C: 29.2%. 19/65 Recorre 19 de las 65 líneas totales
			 * D:Intencionadamente en blanco. No se comprueban datos
			 */
			{
				"member1", "parade1", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);

	}

	protected void templateCreate(final String username, final int paradeId, final Class<?> expected) {
		Class<?> caught;
		Request request, requestSaved;
		Parade parade;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);
			parade = this.paradeService.findOne(paradeId);

			request = this.requestService.create(parade);
			requestSaved = this.requestService.saveNew(request);
			this.requestService.flush();

			Assert.notNull(requestSaved);
			Assert.isTrue(requestSaved.getId() != 0);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			/*
			 * A: Acme-Madrugá Req.7,10.6 Editar row y column de una solicitud aprobada
			 * C:100%23/23 Recorre 23 de las 23 líneas totales
			 * D:14.25% 1/7
			 */
			{
				"brotherhood1", "request1", 3, 3, null, null
			},

			/*
			 * EL TEST HACE QUE NO PODAMOS MIRAR EL OBJETO EN EL POPULATE PARA COMPROBAR EL STATUS QUE TENÍAMOS
			 * A: Acme-Madrugá Req.7,10.6 Editar una solicitud
			 * B:Test negativo: Editar row y column una solicitud aprobada que es de otra hermandad
			 * C:87%20/23 Recorre 20 de las 23 líneas totales
			 * D:14.25%1/7
			 */
			{
				"brotherhood3", "request1", 3, 3, null, IllegalArgumentException.class
			},
			/*
			 * A: Acme-Madrugá Req.7,10.6 Editar una solicitud
			 * B:Test negativo: Editar row y column de una solicitud aprobada a unas posiciones ocupadas en la parade
			 * C:20/23 Recorre 20 de las 23 líneas totales
			 * D:14.25%1/7
			 */
			{
				"brotherhood2", "request6", 2, 1, null, IllegalArgumentException.class
			},
			/*
			 * A: Acme-Madrugá Req.7,10.6 Editar una solicitud
			 * B:Test negativo: Editar reasonWhy de una solicitud aprobada
			 * C:20/23 Recorre 20 de las 23 líneas totales
			 * D:14.25%1/7
			 */
			{
				"brotherhood2", "request6", 2, 2, "Reason why escrito", IllegalArgumentException.class
			},

			/*
			 * EL TEST HACE QUE NO PODAMOS MIRAR EL OBJETO EN EL POPULATE PARA COMPROBAR EL STATUS QUE TENÍAMOS
			 * A: Acme-Madrugá Req.7,10.6 Editar una solicitud
			 * B:Test negativo: Editar row y column de una solicitud con estado denegado
			 * C:87%20/23 Recorre 20 de las 23 líneas totales
			 * D:14.25%1/7
			 */
			{
				"brotherhood2", "request9", 1, 1, "El estado es denegado", IllegalArgumentException.class
			},

			/*
			 * A: Acme-Madrugá Req.7,10.6 Editar una solicitud
			 * B:Test negativo: Editar dejar vacío el reason why una solicitud con estado denegado
			 * C:34% 8/23 Recorre 8 de las 23 líneas totales
			 * D:14.25%1/7
			 */
			{
				"brotherhood2", "request9", null, null, "", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Integer) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}
	protected void templateEdit(final String username, final int requestId, final Integer column, final Integer row, final String reasonWhy, final Class<?> expected) {
		Class<?> caught;
		Request request;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			request = this.requestService.findOne(requestId);
			request.setColumnParade(column);
			request.setRowParade(row);
			request.setReasonWhy(reasonWhy);
			this.requestService.saveEdit(request);
			this.requestService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverChangeStatus() {
		final Object testingData[][] = {
			/*
			 * A: Acme-Madrugá Req.7,10.6 Cambiar el estado a aceptado
			 * C:100% 40/40 Recorre 40 de las 40 líneas totales
			 * D:20% 1/5
			 */
			{
				"brotherhood1", "request01", null, "APPROVED", null
			},

			/*
			 * A: Acme-Madrugá Req.7,10.6 Cambiar el estado a rechazado y ponerle motivo de cancelación
			 * C:100% 31/31 Recorre 31 de las 31 líneas totales
			 * D:20% 1/5
			 */
			{
				"brotherhood1", "request01", "Es inapropiado", "REJECTED", null
			},

			/*
			 * A: Acme-Madrugá Req.7,10.6 Cambiar el estado de una request
			 * B: El autenticado no es el dueño de la parade a la que hace referencia la request
			 * C:6.11% 21/40 Recorre 21 de las 40 líneas totales
			 * D:20% 1/5
			 */
			{
				"brotherhood2", "request01", null, "APPROVED", IllegalArgumentException.class
			},

			/*
			 * A: Acme-Madrugá Req.7, 10.6 Cambiar el estado
			 * B: Test negativo: Cambiar el estado a rechazado y dejar en blanco el motivo de cancelación
			 * C: 77.4% 24/31 Recorre 24 de las 31 líneas totales
			 * D:20% 1/5
			 */
			{
				"brotherhood1", "request01", "", "REJECTED", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateChangeStatus((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}
	protected void templateChangeStatus(final String username, final int requestId, final String reasonWhy, final String status, final Class<?> expected) {
		Class<?> caught;
		Request request;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			request = this.requestService.findOne(requestId);

			if (status == "APPROVED")
				this.requestService.saveEditApproved(request);
			else if (status == "REJECTED") {
				request.setReasonWhy(reasonWhy);
				this.requestService.saveEditRejected(request);

			}
			this.requestService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * A:Acme-Madrugá Req.7,11.1 Mostrar una solicitud
	 * B:Ver el request de otro usuario
	 * C:96.7% 30/31 Recorre 30 de las 31 líneas totales
	 * D: Intencionadamente en blanco. No se comprueban datos
	 */
	@Test(expected = IllegalArgumentException.class)
	public void displayMember_negative_test() {
		super.authenticate("member2");

		int requestId;

		requestId = super.getEntityId("request1");
		this.requestService.findOneToMember(requestId);

		super.unauthenticate();
	}

	/*
	 * A:Acme-Madrugá Req.7,11.1 Mostrar una solicitud
	 * C:96.7% 30/31 Recorre 30 de las 31 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test
	public void displayMember_positive_test() {
		super.authenticate("member1");

		int requestId;

		requestId = super.getEntityId("request1");
		this.requestService.findOneToMember(requestId);

		super.unauthenticate();
	}

	/*
	 * A:Acme-Madrugá Req.7,10.6 Mostrar una solicitud
	 * B:Ver el request de otro usuario y el autenticado es una hermandad
	 * C:95.8%23/24 Recorre 23 de las 24 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test(expected = IllegalArgumentException.class)
	public void displayBrotherhood_negative_test() {
		super.authenticate("brotherhood2");

		int requestId;

		requestId = super.getEntityId("request1");
		this.requestService.findOneToBrotherhood(requestId);

		super.unauthenticate();
	}

	/*
	 * A: Acme-Madrugá Req.7,10.6 Mostrar una solicitud
	 * C:100%24/24 Recorre 24 de las 24 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test
	public void displayBrotherhood_positive_test() {
		super.authenticate("brotherhood1");

		int requestId;

		requestId = super.getEntityId("request1");
		this.requestService.findOneToBrotherhood(requestId);

		super.unauthenticate();
	}

	/*
	 * A: Acme-Madrugá Req.7,11.1 Borrar una solicitud
	 * C:100%30/30 Recorre 30 de las 30 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test
	public void delete_positive_test() {
		super.authenticate("member1");

		int requestId;
		Request request;

		requestId = super.getEntityId("request01");
		request = this.requestService.findOne(requestId);

		this.requestService.deleteCancel(request);

		super.unauthenticate();
	}

	/*
	 * A: Acme-Madrugá Req.7,11.1 Borrar una solicitud
	 * B:Borrar una request con estado approved
	 * C:12.5% 3/24 Recorre 3 de las 24 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_negativo_test() {
		super.authenticate("member1");

		int requestId;
		Request request;

		requestId = super.getEntityId("request1");
		request = this.requestService.findOne(requestId);

		this.requestService.deleteCancel(request);

		super.unauthenticate();
	}

	/*
	 * A: Acme-Madrugá Req.7,11.1 Borrar una solicitud
	 * B:Borrar una request de otro usuario
	 * C:95.8% 23/24 Recorre 23 de las 24 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_negative1_test() {
		super.authenticate("member3");

		int requestId;
		Request request;

		requestId = super.getEntityId("request01");
		request = this.requestService.findOne(requestId);

		this.requestService.deleteCancel(request);

		super.unauthenticate();
	}

	/*
	 * A: Acme-Madrugá Req.7,11.1 Borrar una solicitud
	 * B:No se puede borrar una request con estado rejected
	 * C:12.5% 3/24 Recorre 3 de las 24 líneas totales
	 * D:Intencionadamente en blanco. No se comprueban datos
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_negative2_test() {
		super.authenticate("member11");

		int requestId;
		Request request;

		requestId = super.getEntityId("request9");
		request = this.requestService.findOne(requestId);

		this.requestService.deleteCancel(request);

		super.unauthenticate();
	}
}
