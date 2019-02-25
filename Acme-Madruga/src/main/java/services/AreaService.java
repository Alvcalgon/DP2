
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


	// Constructors -------------------------------
	public AreaService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Area findOne(final int areaId) {
		Area result;

		result = this.areaRepository.findOne(areaId);

		return result;
	}

	public Area create() {
		Area result;

		result = new Area();

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
		this.checkUnusedArea(area);

		Assert.isTrue(this.areaRepository.exists(area.getId()));

		this.areaRepository.delete(area);

	}

	public void findOneToEdit(final int brotherhoodId) {
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(principal.getId() == brotherhoodId);
	}

	// Protected methods --------------------------
	protected Collection<Brotherhood> findBrotherhoodFromArea(final Area area) {
		Collection<Brotherhood> result;

		result = this.areaRepository.findBrotherhoodFromArea(area);

		return result;
	}

	// Private methods ---------------------------

	private void checkUnusedArea(final Area area) {
		Assert.isTrue(this.findBrotherhoodFromArea(area).isEmpty());

	}

}
