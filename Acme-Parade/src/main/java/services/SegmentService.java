
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SegmentRepository;
import domain.Brotherhood;
import domain.GPSCoordinates;
import domain.Segment;
import forms.SegmentForm;

@Service
@Transactional
public class SegmentService {

	// Managed repository --------------------------

	@Autowired
	private SegmentRepository	segmentRepository;

	// Other supporting services -------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private Validator			validator;


	// Constructors -------------------------------

	public SegmentService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Segment create() {
		//TODO: terminar
		Segment result;

		result = new Segment();

		return result;

	}

	public Segment save(final Segment segment) {
		Assert.notNull(segment);

		Segment result;

		result = this.segmentRepository.save(segment);

		return result;

	}

	public void delete(final Segment segment) {
		//TODO

	}
	public Segment findOne(final int segmentId) {
		Segment result;

		result = this.segmentRepository.findOne(segmentId);

		return result;
	}

	public Collection<Segment> findAll() {
		Collection<Segment> results;

		results = this.segmentRepository.findAll();

		return results;
	}

	public Segment findOneToEdit(final int segmentId) {
		Segment result;
		Brotherhood principal;

		result = this.segmentRepository.findOne(segmentId);
		principal = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.brotherhoodService.findBrotherhoodBySegment(segmentId).equals(principal));

		return result;
	}

	// Other business methods ---------------------

	public SegmentForm createForm(final Segment segment, final int paradeId) {
		SegmentForm segmentForm;
		final GPSCoordinates gpsOrigin;
		final GPSCoordinates gpsDestination;
		segmentForm = new SegmentForm();

		if (segment.getId() == 0) {

			gpsOrigin = new GPSCoordinates();
			gpsDestination = new GPSCoordinates();
		} else {
			gpsOrigin = segment.getOrigin();
			gpsDestination = segment.getDestination();
		}

		segmentForm.setId(segment.getId());
		segmentForm.setOriginLatitude(gpsOrigin.getLatitude());
		segmentForm.setOriginLongitude(gpsOrigin.getLongitude());
		segmentForm.setDestinationLatitude(gpsDestination.getLatitude());
		segmentForm.setDestinationLongitude(gpsDestination.getLongitude());
		segmentForm.setReachingOrigin(segment.getReachingOrigin());
		segmentForm.setReachingDestination(segment.getReachingDestination());
		segmentForm.setParadeId(paradeId);

		return segmentForm;

	}

	public Segment reconstruct(final SegmentForm segmentForm, final BindingResult binding) {
		final Segment segment;
		final GPSCoordinates gpsOrigin;
		final GPSCoordinates gpsDestination;

		if (segmentForm.getId() == 0) {

			segment = this.create();
			gpsOrigin = new GPSCoordinates();
			gpsDestination = new GPSCoordinates();

		} else {
			segment = this.segmentRepository.findOne(segmentForm.getId());
			gpsOrigin = segment.getOrigin();
			gpsDestination = segment.getDestination();
		}

		gpsOrigin.setLatitude(segmentForm.getOriginLatitude());
		gpsOrigin.setLongitude(segmentForm.getOriginLongitude());
		gpsDestination.setLatitude(segmentForm.getDestinationLatitude());
		gpsDestination.setLongitude(segmentForm.getDestinationLongitude());

		segment.setDestination(gpsDestination);
		segment.setOrigin(gpsOrigin);
		segment.setReachingOrigin(segmentForm.getReachingOrigin());
		segment.setReachingDestination(segmentForm.getReachingDestination());

		this.validator.validate(segment, binding);

		return segment;

	}
}
