
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import domain.Area;
import domain.Brotherhood;

@Service
@Transactional
public class AreaService {

	// Managed repository --------------------------
	@Autowired
	private AreaRepository		areaRepository;

	// Other supporting services -------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private UtilityService		utilityService;


	// Constructors -------------------------------
	public AreaService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Area findOne(final int areaId) {
		Area result;

		result = this.areaRepository.findOne(areaId);

		Assert.notNull(result);

		return result;
	}

	public Collection<Area> findAll() {
		Collection<Area> areas;

		areas = this.areaRepository.findAll();

		return areas;
	}

	public Collection<Area> findAllByBrotherhood(final Brotherhood brotherhood) {
		Collection<Area> areas;

		this.checkNotArea(brotherhood);
		areas = this.areaRepository.findAll();

		return areas;
	}

	public Area create() {
		Area result;

		result = new Area();

		return result;
	}

	public Area save(final Area area) {
		Assert.notNull(area);

		Area result;
		this.utilityService.checkPicture(area.getPictures());
		result = this.areaRepository.save(area);

		return result;
	}

	public Area save(final Area area, final int brotherhoodId) {
		Assert.notNull(brotherhoodId);

		Area result;
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		result = this.areaRepository.save(area);
		brotherhood.setArea(result);

		return result;
	}

	public void delete(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(this.findBrotherhoodFromArea(area) == 0);

		Assert.isTrue(this.areaRepository.exists(area.getId()));

		this.areaRepository.delete(area);
	}

	public void findOneToEditBrotherhood(final int brotherhoodId) {
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(principal.getId() == brotherhoodId);
	}

	// Private methods ---------------------------
	private void checkNotArea(final Brotherhood brotherhood) {
		Assert.isTrue(brotherhood.getArea() == null);
	}

	// Other methods --------------------------

	public Integer findBrotherhoodFromArea(final Area area) {
		Integer result;

		result = this.areaRepository.findBrotherhoodFromArea(area.getId());

		return result;
	}

	public Collection<String> findAllAreaNames() {
		Collection<String> result;

		result = this.areaRepository.findAllAreaNames();
		Assert.notNull(result);

		return result;
	}
}
