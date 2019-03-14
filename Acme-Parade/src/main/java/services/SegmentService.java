
package services;

import java.util.Collection;
import java.util.Date;

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
		this.checkSegment(segment, parade);

		result = this.segmentRepository.save(segment);

		if (!isUpdating)
			this.addSegmentToParade(parade, result);

		return result;

	}
	private void checkSegment(final Segment segment, final Parade parade) {
		//Compruebo que la fecha origen es antes que la destino
		Assert.isTrue(segment.getReachingDestination().after(segment.getReachingOrigin()));

		//Comprueba que las fechas del segmento son posteriores a la del desfile
		Assert.isTrue(segment.getReachingDestination().after(parade.getMoment()));
		Assert.isTrue(segment.getReachingOrigin().after(parade.getMoment()));

		//Compruebo que la fecha del segmento actual está entre las fechas del segmento previo y el posterior
		final Date fechaSegmentoPrevio;
		final Date fechaSegmentoPosterior;
		Collection<Segment> segmentsParade;

		segmentsParade = this.findOrderedSegments(parade.getId());

		final int i = 0;
		final int tamaño = segmentsParade.size();
		for (final Segment s : segmentsParade)
			if (s.equals(segment) && i == 0) { //si es el primer segmento del camino 
				if (tamaño != 1) { // si hay mas de un elemento
					//tengo que ver que el final de s coincida con el inicio del segundo
				}
			} else if (s.equals(segment) && i >= 0 && i <= tamaño - 1) { // si no es ni el primero ni el ultimo

				//tengo que mirar tanto el inicio como el fin
			} else if (s.equals(segment) && i == tamaño - 1) {
				//solo tengo que mirar el inicio de s con el final del anterior
			}

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

	public Collection<Segment> findOrderedSegments(final int paradeId) {
		Collection<Segment> segmentsOrdered;

		segmentsOrdered = this.segmentRepository.findOrderedSegments(paradeId);

		return segmentsOrdered;
	}

}
