
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.GPSCoordinates;
import domain.Parade;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SegmentServiceTest extends AbstractTest {

	// The SUT --------------------------

	@Autowired
	private SegmentService	segmentService;

	// Supporting services --------------

	@Autowired
	private ParadeService	paradeService;


	// Tests ------------------------

	/*
	 * A: Brotherhood creates a segment, when it's creates their attributes are null.
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void create_test() {
		super.authenticate("brotherhood1");

		Segment segment;

		segment = this.segmentService.create();

		Assert.isNull(segment.getOrigin());
		Assert.isNull(segment.getDestination());
		Assert.isNull(segment.getReachingOrigin());
		Assert.isNull(segment.getReachingDestination());

		super.unauthenticate();

	}

	@Test
	public void driverInsert() {
		final Object testingData[][] = {
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Brotherhood creates a segment from a parade that not belongs to him
			 * C: Approximately 69% of sentence coverage
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade2", 40.379869, -7.997886, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::origin in latitudeOrigin exceeds the maximum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 91.42, -7.997886, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::origin in latitudeOrigin exceeds the minimum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", -91.42, -7.997886, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::origin in longitudeOrigin exceeds the maximum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, 181.222222, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::origin longitudeOrigin exceeds the minimum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -181.222222, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::destination in latitudeDestination exceeds the maximum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, 91.000000, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", ConstraintViolationException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::destination in latitudeDestination exceeds the minimum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, -90.99999, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", ConstraintViolationException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::destination in longitudeDestination exceeds the maximum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, 41.379869, 180.524886, "2020-02-02 14:30", "2020-02-02 16:30", ConstraintViolationException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Invalid data in Segment::destination in longitudeDestination exceeds the maximum allowed
			 * C: Approximately 80% of sentence coverage.
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, 41.379869, -180.524886, "2020-02-02 14:30", "2020-02-02 16:30", ConstraintViolationException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Segment's origin is not the destination of the previous segment
			 * C: Approximately 70% of sentence coverage,
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 41.379869, -8.997886, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Segment::reachingOrigin is not equal to Segment::reachingDestination of the previous segment.
			 * C: Approximately 70% of sentence coverage,
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade2", 40.379869, -7.997886, 41.379869, -7.524886, "2020-02-02 15:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * B: Segment::reachingOrigin is before Parade::moment.
			 * C: Approximately 60% of sentence coverage,
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, 41.379869, -7.524886, "2019-05-26 14:30", "2020-02-02 16:30", IllegalArgumentException.class
			},
			/*
			 * A: Brotherhood creates a segment from a parade
			 * C: 100% of sentence coverage
			 * D: 16,6% (1/6) of data coverage. GPSCoordinates has 2 attributes which only can writes right values, and Segment has 2 attributes of type GPSCoordinates that sum 4 combinations. And 2 attributes
			 * of type Date that can´t be null so only right values.
			 */
			{
				"brotherhood1", "parade1", 40.379869, -7.997886, 41.379869, -7.524886, "2020-02-02 14:30", "2020-02-02 16:30", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateInsert((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (double) testingData[i][2], (double) testingData[i][3], (double) testingData[i][4], (double) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	protected void templateInsert(final String username, final int paradeId, final double latitudeOrigin, final double longitudeOrigin, final double latitudeDestination, final double longitudeDestination, final String reachingOrigin,
		final String reachingDestination, final Class<?> expected) {
		Class<?> caught;
		Parade parade;
		Segment segment, saved;
		final GPSCoordinates origin, destination;
		DateFormat formatter;
		Date dateReachingOrigin, dateReachingDestination;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			origin = new GPSCoordinates();
			origin.setLatitude(latitudeOrigin);
			origin.setLongitude(longitudeOrigin);

			destination = new GPSCoordinates();
			destination.setLatitude(latitudeDestination);
			destination.setLongitude(longitudeDestination);

			segment = new Segment();
			segment.setOrigin(origin);
			segment.setDestination(destination);

			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateReachingOrigin = formatter.parse(reachingOrigin);
			dateReachingDestination = formatter.parse(reachingDestination);

			segment.setReachingOrigin(dateReachingOrigin);
			segment.setReachingDestination(dateReachingDestination);

			parade = this.paradeService.findOne(paradeId);

			saved = this.segmentService.save(segment, parade);
			this.segmentService.flush();

			Assert.notNull(saved);
			Assert.isTrue(saved.getId() != 0);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * A: Brotherhood display a segment
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void display_positive_test() {
		super.authenticate("brotherhood1");

		Segment segment;

		segment = this.segmentService.findOneToEdit(super.getEntityId("segment3"));

		Assert.notNull(segment);

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood display a segment
	 * B: a brotherhood try to display a other brotherhood's segment.
	 * C: 80% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test(expected = IllegalArgumentException.class)
	public void display_negative_test() {
		super.authenticate("brotherhood1");

		Segment segment;

		segment = this.segmentService.findOneToEdit(super.getEntityId("segment6"));

		Assert.notNull(segment);

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood list the paths of their parades
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void list_positive_test() {
		super.authenticate("brotherhood1");

		Parade parade;
		Collection<Segment> segments;

		parade = this.paradeService.findOneToDisplay(super.getEntityId("parade1"));

		segments = parade.getSegments();

		Assert.notNull(segments);

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood list the paths of their parades
	 * B: Brotherhood try to list the paths of a parade that is not in final mode
	 * C: 90% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test(expected = IllegalArgumentException.class)
	public void list_negative_test() {
		super.authenticate("brotherhood2");

		Parade parade;
		Collection<Segment> segments;

		parade = this.paradeService.findOneToDisplay(super.getEntityId("parade6"));

		segments = parade.getSegments();

		Assert.notNull(segments);

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood updates a segment
	 * C: 85% of sentence coverage
	 * D: 25% of data coverage. (Only 1/4 attribute updates)
	 */
	@Test
	public void update_positive_test() {
		super.authenticate("brotherhood1");

		Parade parade;
		Segment segment;
		DateFormat formatter;
		final Date dateReachingDestination;
		String reachingDestination;

		parade = this.paradeService.findOne(super.getEntityId("parade1"));
		segment = this.segmentService.findOne(super.getEntityId("segment3"));

		reachingDestination = "2020-02-02 16:30";

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			dateReachingDestination = formatter.parse(reachingDestination);

			segment.setReachingDestination(dateReachingDestination);

			this.segmentService.save(segment, parade);
		} catch (final Throwable oops) {
			Assert.isNull(oops);
		}

		super.unauthenticate();

	}
	/*
	 * A: Brotherhood updates a segment
	 * B: Segment::reachingDestination is before to reachingOrigin
	 * C: 50% of sentence coverage
	 * D: 25% of data coverage. (Only 1/4 attribute updates)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void update_negative_test() {
		super.authenticate("brotherhood1");

		Parade parade;
		Segment segment;
		DateFormat formatter;
		Date dateReachingDestination;
		String reachingDestination;

		parade = this.paradeService.findOne(super.getEntityId("parade1"));
		segment = this.segmentService.findOne(super.getEntityId("segment3"));

		reachingDestination = "2020-02-02 11:30:00";
		dateReachingDestination = null;

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			dateReachingDestination = formatter.parse(reachingDestination);
		} catch (final Throwable oops) {

		}

		segment.setReachingDestination(dateReachingDestination);

		this.segmentService.save(segment, parade);

		super.unauthenticate();

	}

	/*
	 * A: Brotherhood deletes a segment
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void delete_positive_test() {
		super.authenticate("brotherhood1");

		Parade parade;
		Segment segment;

		parade = this.paradeService.findOne(super.getEntityId("parade1"));
		segment = this.segmentService.findOne(super.getEntityId("segment3"));

		this.segmentService.delete(segment);

		Assert.isTrue(!parade.getSegments().contains(segment));

		super.unauthenticate();

	}

	/*
	 * A: Brotherhood deletes a segment
	 * B: Brotherhood tries to delete a segment that not belongs to him/her parades.
	 * C: 50% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test(expected = NullPointerException.class)
	public void delete_negative_test() {
		super.authenticate("brotherhood1");

		Segment segment;

		segment = this.segmentService.findOne(super.getEntityId("segment4"));

		this.segmentService.delete(segment);

		super.unauthenticate();

	}

}
