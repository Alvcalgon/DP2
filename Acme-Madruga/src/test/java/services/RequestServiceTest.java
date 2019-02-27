
package services;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	//Service under test ----------------------------------
	@Autowired
	private RequestService		requestService;

	@Autowired
	private ProcessionService	processionService;

	//Test ------------------------------------------------

	//	@Test
	//	public void testCreate() {
	//		super.authenticate("member12");
	//		final Integer[] rowFree;
	//		final Integer[] columnFree;
	//		Procession procession;
	//
	//		procession = this.processionService.findOne(super.getEntityId("procession1"));
	//		rowFree = this.processionService.rowFree(procession);
	//		columnFree = this.processionService.columnFree(procession);
	//
	//		System.out.println(rowFree);
	//		System.out.println(columnFree);
	//		super.unauthenticate();
	//	}

	//
	//	@Test
	//	public void testCreate5() {
	//		super.authenticate("member2");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession3"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}
	//
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testCreate6() {
	//		super.authenticate("member3");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession1"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}
	//
	//	@Test
	//	public void testCreate7() {
	//		super.authenticate("member2");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession5"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * No se puede crear request ya que ese miembro se dio de baja esa hermandad
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testCreate2() {
	//		super.authenticate("member3");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession5"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * No se puede crear request ya que ese miembro no está en esa hermandad
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testCreate3() {
	//		super.authenticate("member3");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession1"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}
	//
	//	/*
	//	 * No se puede crear request ya que ese miembro ya realizo una solucitud a dicha cofradia
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testCreate4() {
	//		super.authenticate("member3");
	//		final Request request;
	//		Procession procession;
	//		procession = this.processionService.findOne(super.getEntityId("procession3"));
	//		request = this.requestService.create(procession);
	//		Assert.isTrue(request.getStatus().equals("PENDING"));
	//		Assert.notNull(request);
	//		super.unauthenticate();
	//	}

	//	/*
	//	 * Can not edit request which status is accepted
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testSaveNegative() {
	//		super.authenticate("handyworker2");
	//		final Request request;
	//		final Request requestSaved;
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		request.setStatus("ACCEPTED");
	//
	//		requestSaved = this.requestService.save(request);
	//
	//		Assert.isNull(requestSaved);
	//
	//		super.unauthenticate();
	//	}
	//	/*
	//	 * Can not edit request which status is rejected
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testSaveNegative1() {
	//		super.authenticate("handyworker2");
	//		final Request request;
	//		final Request requestSaved;
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		request.setStatus("REJECTED");
	//
	//		requestSaved = this.requestService.save(request);
	//
	//		Assert.isNull(requestSaved);
	//
	//		super.unauthenticate();
	//	}
	//	/*
	//	 * Can not change request which handyWorker has not curriculum
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testSaveNegative3() {
	//		super.authenticate("handyworker2");
	//		final Request request;
	//		final Request requestSaved;
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		request.setStatus("REJECTED");
	//		requestSaved = this.requestService.save(request);
	//
	//		Assert.isNull(requestSaved);
	//
	//		super.unauthenticate();
	//	}
	//	@Test
	//	public void testFindAll() {
	//		Collection<Request> requests;
	//		requests = this.requestService.findAll();
	//		Assert.notEmpty(requests);
	//		Assert.notNull(requests);
	//
	//	}
	//
	//	@Test
	//	public void testFindOne() {
	//		Request request;
	//
	//		request = this.requestService.findOne(super.getEntityId("request2"));
	//		Assert.notNull(request);
	//
	//	}
	//
	//	@Test
	//	public void testfindDataOfRequestPrice() {
	//		Double[] result;
	//
	//		result = this.requestService.findDataOfRequestPrice();
	//		Assert.notNull(result);
	//	}
	//	@Test
	//	public void testfindRatioPendingRequests() {
	//		Double result;
	//
	//		result = this.requestService.findRatioPendingRequests();
	//
	//		Assert.notNull(result);
	//	}
	//	@Test
	//	public void testfindRatioAcceptedRequests() {
	//		Double result;
	//
	//		result = this.requestService.findRatioAcceptedRequests();
	//
	//		Assert.notNull(result);
	//	}
	//	@Test
	//	public void testfindRatioRejectedRequests() {
	//		Double result;
	//
	//		result = this.requestService.findRatioRejectedRequests();
	//
	//		Assert.notNull(result);
	//	}
	//	@Test
	//	public void testfindRatioPendingRequestsNotChangeStatus() {
	//		Double result;
	//
	//		result = this.requestService.findRatioPendingRequestsNotChangeStatus();
	//
	//		Assert.notNull(result);
	//	}
	//
	//	@Test
	//	public void testCreckCreditCard() {
	//		super.authenticate("handyWorker1");
	//		final Request request;
	//		CreditCard creditCard;
	//
	//		creditCard = new CreditCard();
	//		creditCard.setBrandName("maria");
	//		creditCard.setCvvCode(123);
	//		creditCard.setExpirationMonth("02");
	//		creditCard.setHolderName("maria");
	//		creditCard.setExpirationYear("23");
	//		creditCard.setNumber("6702386065238009");
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		this.requestService.addCreditCard(request, creditCard);
	//		Assert.isTrue(request.getCreditCard().equals(creditCard));
	//		super.unauthenticate();
	//	}
	//
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testCreckCreditCard1() {
	//		super.authenticate("handyWorker1");
	//		final Request request;
	//		CreditCard creditCard;
	//
	//		creditCard = new CreditCard();
	//		creditCard.setBrandName("maria");
	//		creditCard.setCvvCode(023);
	//		creditCard.setExpirationMonth("0");
	//		creditCard.setHolderName("maria jimenez");
	//		creditCard.setExpirationYear("23");
	//		creditCard.setNumber("679");
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		this.requestService.addCreditCard(request, creditCard);
	//		Assert.isTrue(request.getCreditCard().equals(creditCard));
	//		super.unauthenticate();
	//	}
	//	/*
	//	 * Credit card which number is not correct
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void testNegativeCreckCreditCard() {
	//		super.authenticate("handyWorker1");
	//		final Request request;
	//		CreditCard creditCard;
	//
	//		creditCard = new CreditCard();
	//		creditCard.setBrandName("maria");
	//		creditCard.setCvvCode(123);
	//		creditCard.setExpirationMonth("08");
	//		creditCard.setHolderName("maria");
	//		creditCard.setExpirationYear("22");
	//		creditCard.setNumber("670209");
	//
	//		request = this.requestService.findOne(super.getEntityId("request3"));
	//		this.requestService.addCreditCard(request, creditCard);
	//		Assert.isTrue(request.getCreditCard().equals(creditCard));
	//		super.unauthenticate();
	//	}
}
