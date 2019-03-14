
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

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
	private ParadeService		paradeService;


	// Constructors -------------------------------

	public SegmentService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Segment create() {
		Segment result;

		result = new Segment();

		return result;

	}

	public Segment save(final Segment segment, Parade parade) {
		Assert.notNull(segment);

		Segment result;
		boolean isUpdating;

		isUpdating = this.segmentRepository.exists(segment.getId());

		if (isUpdating) {

			parade = this.paradeService.findBySegment(segment.getId());
			this.paradeService.checkParadeByBrotherhood(parade);
		} else
			this.paradeService.checkParadeByBrotherhood(parade);

		Assert.notNull(parade);

		result = this.segmentRepository.save(segment);

		if (!isUpdating)
			this.addSegmentToParade(parade, result);

		return result;

	}

	public void delete(final Segment segment) {
		Assert.notNull(segment);
		Assert.isTrue(this.segmentRepository.exists(segment.getId()));

		Parade parade;
		Brotherhood principal;

		parade = this.paradeService.findBySegment(segment.getId());
		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.paradeService.getBrotherhoodToParade(parade).equals(principal));

		this.removeSegmentToParade(parade, segment);

		this.segmentRepository.delete(segment);

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

	private void removeSegmentToParade(final Parade parade, final Segment segment) {
		Collection<Segment> segments;

		segments = parade.getSegments();
		segments.remove(segment);

	}

	private void addSegmentToParade(final Parade parade, final Segment segment) {
		Collection<Segment> segments;

		segments = parade.getSegments();
		segments.add(segment);

	}
}
