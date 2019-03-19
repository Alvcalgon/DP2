
package services;

import java.text.SimpleDateFormat;
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
		this.paradeService.checkParadeByBrotherhood(parade);
		this.utilityService.checkMoment(segment.getReachingOrigin(), segment.getReachingDestination());

		List<Segment> segments;
		Segment result, previousSegment, nextSegment;
		int pos, tam;

		segments = this.findOrderedSegments(parade.getId());

		pos = segments.indexOf(segment);
		tam = segments.size();

		// The parade has not any segment yet, so this segment is the first in the path
		if (pos == -1 && tam == 0) {
			Assert.isTrue(segment.getReachingOrigin().equals(parade.getMoment()));

			result = this.segmentRepository.save(segment);
			this.paradeService.addSegment(parade, result);

			// The segment is been inserted and the parade has, at least, one segment in the path
		} else if (pos == -1 && tam > 0) {
			this.utilityService.checkMoment(parade.getMoment(), segment.getReachingOrigin());

			previousSegment = segments.get(tam - 1);

			// The segments must be contiguous
			Assert.isTrue(previousSegment.getReachingDestination().equals(segment.getReachingOrigin()));
			Assert.isTrue(previousSegment.getDestination().equals(segment.getOrigin()));

			result = this.segmentRepository.save(segment);
			this.paradeService.addSegment(parade, result);

			// The first segment is been edited and path has not other segments.	
		} else if (pos == 0 && tam == 1) {
			Assert.isTrue(segment.getReachingOrigin().equals(parade.getMoment()));

			result = this.segmentRepository.save(segment);

			// The first segment is been edited and path has another segments. So we
			// must update the second segment
		} else if (pos == 0 && tam > 1) {
			Assert.isTrue(segment.getReachingOrigin().equals(parade.getMoment()));

			nextSegment = segments.get(1);
			nextSegment.setOrigin(segment.getDestination());
			nextSegment.setReachingOrigin(segment.getReachingDestination());

			result = this.segmentRepository.save(segment);

			// The last segment is been edited
		} else if (pos == (tam - 1) && tam > 1) {
			this.utilityService.checkMoment(parade.getMoment(), segment.getReachingOrigin());

			previousSegment = segments.get(pos - 1);
			previousSegment.setDestination(segment.getOrigin());
			previousSegment.setReachingDestination(segment.getReachingOrigin());

			result = this.segmentRepository.save(segment);

			// The segment doesn't take up the first position nor last position 
		} else if (pos > 0 && pos < (tam - 1)) {
			this.utilityService.checkMoment(parade.getMoment(), segment.getReachingOrigin());

			nextSegment = segments.get(pos + 1);
			nextSegment.setOrigin(segment.getDestination());
			nextSegment.setReachingOrigin(segment.getReachingDestination());

			previousSegment = segments.get(pos - 1);
			previousSegment.setDestination(segment.getOrigin());
			previousSegment.setReachingDestination(segment.getReachingOrigin());

			result = this.segmentRepository.save(segment);
		}

		this.checkSegment(segment, parade);
		result = this.segmentRepository.save(segment);

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

	public Collection<Segment> findAll() {
		Collection<Segment> results;

		results = this.segmentRepository.findAll();

		return results;
	}

	public Segment findOneToEdit(final int segmentId) {
		Segment result;

		result = this.segmentRepository.findOne(segmentId);

		Assert.notNull(result);
		this.checkPrincipalBySegment(segmentId);

		return result;
	}

	// Other business methods ---------------------

	public List<Segment> findOrderedSegments(final int paradeId) {
		List<Segment> segmentsOrdered;

		segmentsOrdered = this.segmentRepository.findOrderedSegments(paradeId);

		return segmentsOrdered;
	}
	public Boolean isDeletable(final Segment segment) {
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

	// Private methods ---------------------

	private void removeSegmentToParade(final Segment segment) {
		Collection<Segment> segments;
		Parade parade;

		parade = this.paradeService.findBySegment(segment.getId());

		segments = parade.getSegments();
		segments.remove(segment);

	}

	private void checkSegment(final Segment segment, final Parade parade) {
		if (!segment.getReachingDestination().after(segment.getReachingOrigin()))
			throw new DataIntegrityViolationException("Invalid date");

		if (!segment.getReachingDestination().after(parade.getMoment()) || !segment.getReachingOrigin().after(parade.getMoment()))
			throw new DataIntegrityViolationException("Invalid dates");
	}

	private void checkSegmentEdit(final Segment segment, final Parade parade) {
		Collection<Segment> segmentsParadeCollection;
		Segment segmentChecked;

		segmentsParadeCollection = parade.getSegments();

		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;
			segmentsParade = this.findOrderedSegments(parade.getId());

			//We run the path to check the data of the segments

			int i = 0;
			for (final Segment s : segmentsParade) {

				if (s.equals(segment))
					segmentChecked = segment;
				else
					segmentChecked = s;

				if (i == 0) {
					if (!segmentChecked.getReachingDestination().equals(segmentsParade.get(i + 1).getReachingOrigin()) || segmentChecked.getDestination().equals(segmentsParade.get(i + 1).getOrigin()))
						throw new DataIntegrityViolationException("Invalid data");
				} else if (i > 0 && i < segmentsParade.size() - 1) {
					if ((segmentChecked.getReachingDestination().compareTo(segmentsParade.get(i + 1).getReachingOrigin())) != 0 || (segmentChecked.getReachingOrigin().compareTo(segmentsParade.get(i - 1).getReachingDestination())) != 0
						|| (segmentChecked.getDestination().getLatitude() != (segmentsParade.get(i + 1).getOrigin().getLatitude())) || (segmentChecked.getDestination().getLongitude() != (segmentsParade.get(i + 1).getOrigin().getLongitude()))
						|| (segmentChecked.getOrigin().getLatitude() != (segmentsParade.get(i - 1).getDestination().getLatitude())) || (segmentChecked.getOrigin().getLongitude()) != (segmentsParade.get(i - 1).getDestination().getLongitude()))
						throw new DataIntegrityViolationException("Invalid data");
				} else if (i == segmentsParade.size() - 1)
					if ((segmentChecked.getReachingOrigin().compareTo(segmentsParade.get(i - 1).getReachingDestination())) != 0 || segmentChecked.getOrigin().getLatitude() != (segmentsParade.get(i - 1).getDestination().getLatitude())
						|| segmentChecked.getOrigin().getLongitude() != (segmentsParade.get(i - 1).getDestination().getLongitude()))
						throw new DataIntegrityViolationException("Invalid data");

				i = i + 1;
			}

		}

	}
	private void checkSegmentCreate(final Segment segment, final Parade parade) {

		// check that the date of the current segment is between the dates of the previous segment and the subsequent segment (depends on your position on the path)
		Collection<Segment> segmentsParadeCollection;
		Segment segmentLast;
		final SimpleDateFormat formatter;
		final String segementOriginString;
		final String segmentLastDestinationString;
		final int tamaño;

		segmentsParadeCollection = parade.getSegments();
		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;

			segmentsParade = this.findOrderedSegments(parade.getId());
			tamaño = segmentsParade.size();
			segmentLast = segmentsParade.get(tamaño - 1); // take the last segment
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			segementOriginString = formatter.format(segment.getReachingOrigin());
			segmentLastDestinationString = formatter.format(segmentLast.getReachingDestination());

			if (!segementOriginString.equals(segmentLastDestinationString) || !(segment.getOrigin().getLatitude() == (segmentLast.getDestination().getLatitude())) || !(segment.getOrigin().getLongitude() == (segmentLast.getDestination().getLongitude())))
				throw new DataIntegrityViolationException("Invalid data");

		}

	}

	private void checkPrincipalBySegment(final int segmentId) {

		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.brotherhoodService.findBrotherhoodBySegment(segmentId).equals(principal.getId()));

	}

}
