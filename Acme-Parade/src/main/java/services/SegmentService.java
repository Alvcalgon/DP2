
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Brotherhood;
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
		//TODO: terminar
		Assert.notNull(segment);
		Assert.isTrue(segment.getId() == 0);

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
		Brotherhood brotherhood;

		result = this.segmentRepository.findOne(segmentId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.brotherhoodService.findBrotherhoodBySegment(result.getId()).equals(brotherhood));

		return result;
	}

	// Other business methods ---------------------

}
