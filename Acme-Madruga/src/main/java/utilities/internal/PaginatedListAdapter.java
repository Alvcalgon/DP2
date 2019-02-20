
package utilities.internal;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.springframework.data.domain.Page;

public class PaginatedListAdapter implements PaginatedList {

	private final Page<?>		pageableQuery;
	private final int			fullListSize;
	private final List<?>		list;
	private final int			objectsPerPage;
	private final int			pageNumber;
	private final String		searchId;
	private final String		sortCriterion;
	private final SortOrderEnum	sortDirection;


	public PaginatedListAdapter(final Page<?> pageableQuery, final String sortProperty) {
		super();

		this.pageableQuery = pageableQuery;
		this.fullListSize = (int) this.pageableQuery.getTotalElements();
		this.list = this.pageableQuery.getContent();
		this.objectsPerPage = this.pageableQuery.getSize();
		this.pageNumber = this.pageableQuery.getNumber() + 1;
		this.searchId = null;

		if (sortProperty == null) {
			this.sortCriterion = null;
			this.sortDirection = null;
		} else {
			this.sortCriterion = sortProperty;

			if (this.pageableQuery.getSort().getOrderFor(sortProperty).isAscending())
				this.sortDirection = SortOrderEnum.ASCENDING;
			else
				this.sortDirection = SortOrderEnum.DESCENDING;
		}
	}

	@Override
	public int getFullListSize() {
		return this.fullListSize;
	}

	@Override
	public List<?> getList() {
		return this.list;
	}

	@Override
	public int getObjectsPerPage() {
		return this.objectsPerPage;
	}

	@Override
	public int getPageNumber() {
		return this.pageNumber;
	}

	@Override
	public String getSearchId() {
		return this.searchId;
	}

	@Override
	public String getSortCriterion() {
		return this.sortCriterion;
	}

	@Override
	public SortOrderEnum getSortDirection() {
		return this.sortDirection;
	}

}
