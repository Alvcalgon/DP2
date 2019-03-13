
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Enrolment;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	// The SUT --------------------------
	@Autowired
	private EnrolmentService	enrolmentService;

	// Supporting services --------------
	@Autowired
	private PositionService		positionService;


	// Tests ------------------------

	/*
	 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void findOne_test_() {
		super.authenticate("brotherhood1");

		final Enrolment enrolment;
		final int enrolmentId = super.getEntityId("enrolment1");

		enrolment = this.enrolmentService.findOneToEdit(enrolmentId);

		Assert.notNull(enrolment);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void findEnrolledMembers_test() {
		super.authenticate("brotherhood1");

		Collection<Enrolment> all;

		all = this.enrolmentService.findRequestEnrolments();

		Assert.isTrue(all != null && !all.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 11.3: List the brotherhoods to which he or she
	 * belongs or has belonged
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void findEnrolmentsByMember_test() {
		super.authenticate("member1");

		Collection<Enrolment> all;

		all = this.enrolmentService.findAllEnrolmentsByPrincipal();

		Assert.isTrue(all != null && !all.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 11.2: Drop out from a brotherhood to which he or she belongs
	 * B: The business rule that is intended to be broken: A member try to drop out brotherhood but this member doesn't
	 * belong to the brotherhood
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void dropOut_negative_test() {
		super.authenticate("member13");

		final int brotherhoodId = super.getEntityId("brotherhood4");

		this.enrolmentService.dropOut(brotherhoodId);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 10.3: When a member is enrolled, a position
	 * must be selected by the brotherhood
	 * B: The business rule that is intended to be broken: the enrolment cannot be edited because it is not active.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveToEditPosition_negative_test() {
		super.authenticate("brotherhood3");

		final int positionId = super.getEntityId("position6");
		final int enrolmentId = super.getEntityId("enrolment6");
		Enrolment enrolment, saved;
		Position position;

		position = this.positionService.findOne(positionId);

		enrolment = this.enrolmentService.findOne(enrolmentId);
		enrolment.setPosition(position);

		saved = this.enrolmentService.saveToEditPosition(enrolment);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 10.3: When a member is enrolled, a position
	 * must be selected by the brotherhood
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void saveToEditPosition_positive_test() {
		super.authenticate("brotherhood1");

		final int positionId = super.getEntityId("position6");
		final int enrolmentId = super.getEntityId("enrolment1");
		Enrolment enrolment, saved;
		Position position;

		position = this.positionService.findOne(positionId);

		enrolment = this.enrolmentService.findOne(enrolmentId);
		enrolment.setPosition(position);

		saved = this.enrolmentService.saveToEditPosition(enrolment);

		Assert.notNull(saved);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level C: requirement 11.2: Drop out from a brotherhood to which he or she belongs
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void dropOut_positive_test() {
		super.authenticate("member1");

		final int brotherhoodId = super.getEntityId("brotherhood1");

		this.enrolmentService.dropOut(brotherhoodId);

		super.unauthenticate();
	}

	@Test
	public void driverReject() {
		final Object testingData[][] = {
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * B: The business rule that is intended to be broken: A brotherhood try to reject an enrolment whose owner is other
			 * brotherhood
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood2", "enrolment21", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * B: The business rule that is intended to be broken: A brotherhood try to reject an enrolment that is not a request.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood1", "enrolment1", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood1", "enrolment21", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateReject((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateReject(final String username, final int enrolmentId, final Class<?> expected) {
		Class<?> caught;
		Enrolment enrolment;

		caught = null;
		try {
			super.authenticate(username);

			enrolment = this.enrolmentService.findOne(enrolmentId);
			this.enrolmentService.reject(enrolment);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverRemove() {
		final Object testingData[][] = {
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * B: The business rule that is intended to be broken: A brotherhood try to remove an enrolment whose owner is other
			 * brotherhood
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood2", "enrolment1", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * B: The business rule that is intended to be broken: the member of this enrolment doesn't
			 * belong currently to the brotherhood of the
			 * enrolment.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood3", "enrolment6", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood1", "enrolment1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRemove((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);

	}

	protected void templateRemove(final String username, final int enrolmentId, final Class<?> expected) {
		Class<?> caught;
		Enrolment enrolment;

		caught = null;
		try {
			super.authenticate(username);

			enrolment = this.enrolmentService.findOne(enrolmentId);
			this.enrolmentService.remove(enrolment);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEnrol() {
		final Object testingData[][] = {
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * B: The business rule that is intended to be broken: this enrolment is not a request. Previously,
			 * the brotherhood takes a decision about this enrolment.
			 * C: Analysis of sentence coverage: Conditional.
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood1", "enrolment1", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 10.3: Manage the members of the brotherhood
			 * C: Analysis of sentence coverage: Conditional: the condition of the if is false
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"brotherhood1", "enrolment21", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEnrol((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateEnrol(final String username, final int enrolmentId, final Class<?> expected) {
		Class<?> caught;
		Enrolment enrolment;

		caught = null;
		try {
			super.authenticate(username);

			enrolment = this.enrolmentService.findOneToEdit(enrolmentId);
			this.enrolmentService.enrol(enrolment);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverRequestEnrolment() {
		final Object testingData[][] = {
			/*
			 * A: Requirement tested: level C: requirement 6
			 * B: The business rule that is intended to be broken: a member has already an active enrolment
			 * associates with a specific brotherhood
			 * C: Analysis of sentence coverage: sequence
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"member1", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 6
			 * B: The business rule that is intended to be broken: a member has already an enrolment
			 * associates with a specific brotherhood. The brotherhood still must deciding what to do with the enrolment.
			 * C: Analysis of sentence coverage: sequence
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"member11", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 6
			 * B: The business rule that is intended to be broken: enrolment::brotherhood still has not a selected area
			 * C: Analysis of sentence coverage: sequence
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"member1", "brotherhood4", IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level C: requirement 6
			 * C: Analysis of sentence coverage: sequence
			 * D: Analysis of data coverage: intentionally blank
			 */
			{
				"member13", "brotherhood2", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestEnrolment((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateRequestEnrolment(final String username, final int brotherhoodId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			this.enrolmentService.requestEnrolment(brotherhoodId);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
