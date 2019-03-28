
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	@Autowired
	private UtilityService		utilityService;


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

	public Segment save(final Segment segment, final Parade parade) {
		Assert.notNull(segment);
		Assert.notNull(parade);
		Segment segmentBBDD;

		segmentBBDD = this.findOne(segment.getId());

		if (segment.getId() != 0)
			Assert.isTrue(this.isEditable(segment));

		this.paradeService.checkParadeByBrotherhood(parade);
		try {
			this.utilityService.checkMoment(segment.getReachingOrigin(), segment.getReachingDestination());
		} catch (final Exception e) {
			throw new DataIntegrityViolationException("Invalid dates");
		}

		try {
			if (segment.getOrigin().getLatitude() == segment.getDestination().getLatitude())
				Assert.isTrue(segment.getOrigin().getLongitude() != segment.getDestination().getLongitude());
		} catch (final Exception e) {
			throw new DataIntegrityViolationException("Invalid gps");
		}

		List<Segment> segments;
		Segment result, previousSegment;
		int pos, tam;
		Long reachingOriginMs, reachingDestinationMs;
		double lat_origin, long_origin, lat_dest, long_dest;

		segments = this.findOrderedSegments(parade.getId());

		pos = segments.indexOf(segment);
		tam = segments.size();

		result = null;

		// The parade has not any segment yet, so this segment is the first in the path
		if (pos == -1 && tam == 0) {
			try {
				Assert.isTrue(segment.getReachingOrigin().equals(parade.getMoment()));
			} catch (final Exception e) {
				throw new DataIntegrityViolationException("Invalid date");
			}

			result = this.segmentRepository.save(segment);
			this.paradeService.addSegment(parade, result);

			// The segment is been inserted and the parade has, at least, one segment in the path
		} else if (pos == -1 && tam > 0) {
			//			try {
			//				this.utilityService.checkMoment(parade.getMoment(), segment.getReachingOrigin());
			//			} catch (final Exception e) {
			//				throw new DataIntegrityViolationException("Invalid date");
			//			}

			previousSegment = segments.get(tam - 1);

			reachingOriginMs = segment.getReachingOrigin().getTime();
			reachingDestinationMs = previousSegment.getReachingDestination().getTime();

			lat_origin = segment.getOrigin().getLatitude();
			long_origin = segment.getOrigin().getLongitude();

			lat_dest = previousSegment.getDestination().getLatitude();
			long_dest = previousSegment.getDestination().getLongitude();

			// The segments must be contiguous
			try {
				Assert.isTrue(reachingDestinationMs.equals(reachingOriginMs));
				Assert.isTrue(lat_dest == lat_origin && long_origin == long_dest);
			} catch (final Exception e) {
				throw new DataIntegrityViolationException("Invalid data");
			}

			result = this.segmentRepository.save(segment);
			this.paradeService.addSegment(parade, result);

			// The first segment is been edited and path has not other segments.	
		} else if (pos == 0) {
			try {
				Assert.isTrue(segment.getReachingOrigin().equals(parade.getMoment()));
				segment.setReachingDestination(segmentBBDD.getReachingDestination());
				segment.setDestination(segmentBBDD.getDestination());
			} catch (final Exception e) {
				throw new DataIntegrityViolationException("Invalid date");
			}

			result = this.segmentRepository.save(segment);

		} else if (pos == (tam - 1) && tam > 1) {

			try {
				this.utilityService.checkMoment(parade.getMoment(), segment.getReachingOrigin());
				segment.setReachingOrigin(segmentBBDD.getReachingOrigin());
				segment.setOrigin(segmentBBDD.getOrigin());
			} catch (final Exception e) {
				throw new DataIntegrityViolationException("Invalid date");
			}

			result = this.segmentRepository.save(segment);

			// The segment doesn't take up the first position nor last position 
		} else if (pos > 0 && pos < (tam - 1))
			throw new DataIntegrityViolationException("Invalid date");

		return result;
	}

	public void delete(final Segment segment) {
		Assert.notNull(segment);
		Assert.isTrue(this.segmentRepository.exists(segment.getId()));

		this.checkPrincipalBySegment(segment.getId());
		Assert.isTrue(this.isDeletable(segment));

		this.removeSegmentToParade(segment);

		this.segmentRepository.delete(segment);

	}

	public Segment findOne(final int segmentId) {
		Segment result;

		result = this.segmentRepository.findOne(segmentId);

		return result;
	}
	public Segment findOneToDisplayChapter(final int segmentId) {
		Segment result;
		Parade parade;

		parade = this.paradeService.findBySegment(segmentId);
		result = this.segmentRepository.findOne(segmentId);

		Assert.notNull(result);
		Assert.isTrue(parade.getIsFinalMode());
		this.paradeService.checkChapter(parade);

		return result;
	}

	public Segment findOneToDisplay(final int segmentId) {
		Segment result;
		Parade parade;

		parade = this.paradeService.findBySegment(segmentId);
		result = this.segmentRepository.findOne(segmentId);

		Assert.notNull(result);
		Assert.isTrue(parade.getIsFinalMode());
		Assert.isTrue(parade.getStatus().equals("accepted"));

		return result;

	}

	public Collection<Segment> findAll() {
		Collection<Segment> results;

		results = this.segmentRepository.findAll();

		return results;
	}

	public Segment findOneToEdit(final int segmentId) {
		Segment result;

		result = this.segmentRepository.findOne(segmentId);

		Assert.notNull(result);
		Assert.isTrue(this.isEditable(result));
		this.checkPrincipalBySegment(segmentId);

		return result;
	}

	// Other business methods ---------------------

	public List<Segment> findOrderedSegments(final int paradeId) {
		List<Segment> segmentsOrdered;

		segmentsOrdered = this.segmentRepository.findOrderedSegments(paradeId);

		return segmentsOrdered;
	}

	public Boolean isFirst(final Segment segment) {

		Boolean result;
		Parade parade;
		List<Segment> segments;

		result = false;
		parade = this.paradeService.findBySegment(segment.getId());
		segments = this.segmentRepository.findOrderedSegments(parade.getId());

		if (segments.indexOf(segment) == 0)
			result = true;

		return result;
	}

	public List<Segment> firstAndLastSegment(final int paradeId) {
		List<Segment> segments;
		List<Segment> segmentList;
		Segment first;
		Segment last;

		segments = new ArrayList<>();
		segmentList = this.findOrderedSegments(paradeId);

		if (segmentList.size() != 0) {
			first = segmentList.get(0);
			segments.add(first);
			if (segmentList.size() > 1) {
				last = segmentList.get(segmentList.size() - 1);

				segments.add(last);
			}

		}

		return segments;
	}

	public Boolean isLast(final Segment segment) {
		Boolean result;
		Parade parade;
		List<Segment> segments;

		result = false;
		parade = this.paradeService.findBySegment(segment.getId());
		segments = this.segmentRepository.findOrderedSegments(parade.getId());

		if (segments.indexOf(segment) == segments.size() - 1)
			result = true;

		return result;

	}

	// Private methods ---------------------
	private void removeSegmentToParade(final Segment segment) {
		Collection<Segment> segments;
		Parade parade;

		parade = this.paradeService.findBySegment(segment.getId());

		segments = parade.getSegments();
		segments.remove(segment);
	}

	private void checkPrincipalBySegment(final int segmentId) {

		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.brotherhoodService.findBrotherhoodBySegment(segmentId).equals(principal.getId()));
	}

	protected void flush() {
		this.segmentRepository.flush();
	}
	public Boolean isDeletable(final Segment segment) {
		Boolean result;
		Parade parade;
		List<Segment> segments;
		Boolean isUnique;

		result = false;
		parade = this.paradeService.findBySegment(segment.getId());
		segments = this.segmentRepository.findOrderedSegments(parade.getId());
		isUnique = segments.size() == 1;

		if ((segments.indexOf(segment) == 0 && isUnique) || segments.indexOf(segment) == segments.size() - 1)
			result = true;

		return result;
	}
	public Boolean isEditable(final Segment segment) {
		Boolean result;
		Parade parade;
		List<Segment> segments;

		result = false;
		parade = this.paradeService.findBySegment(segment.getId());
		segments = this.segmentRepository.findOrderedSegments(parade.getId());

		if (segments.indexOf(segment) == 0 || segments.indexOf(segment) == segments.size() - 1)
			result = true;

		return result;
	}

}
