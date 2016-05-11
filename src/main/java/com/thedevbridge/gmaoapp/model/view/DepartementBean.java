package com.thedevbridge.gmaoapp.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.thedevbridge.gmaoapp.model.Departement;
import java.util.Random;
import javax.annotation.PostConstruct;

/**
 * Backing bean for Departement entities.
 * <p/>
 * This class provides CRUD functionality for all Departement entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DepartementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Departement entities
	 */

	private Long id;
        
        private boolean showTable = false;
        private List<Departement> allDepartement;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Departement departement;

	public Departement getDepartement() {
		return this.departement;
	}

	public void setDepartement(Departement departement) {
		this.departement = departement;
	}
        
        public boolean getShowTable(){
            return this.showTable;
        }

        public void setShowTable(boolean showTable){
            this.showTable = showTable;
        }
        
        public List<Departement> getAllDepartement(){
            return this.allDepartement;
        }
        
        public void setAllDepartement(List<Departement> allDepartement){
            this.allDepartement = allDepartement;
        }
        
        @PostConstruct
        public void loadTechnicien(){
            allDepartement = (List<Departement>)entityManager.createNamedQuery("Departement.findAll").getResultList();
            
            if(allDepartement.isEmpty()){
                this.showTable = false;
            }else{
                this.showTable = true;
            }
        }

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "GmaoApp-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.departement = this.example;
		} else {
			this.departement = findById(getId());
		}
	}

	public Departement findById(Long id) {

		return this.entityManager.find(Departement.class, id);
	}

	/*
	 * Support updating and deleting Departement entities
	 */

	public String update() {
		this.conversation.end();
                
                Random random = new Random();
                
                departement.setIdDepartement(random.nextLong());
                this.entityManager.persist(departement);
                
                String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
                return viewId + "?faces-redirect=true";

		/*try {
			if (this.id == null) {
				this.entityManager.persist(this.departement);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.departement);
				return "view?faces-redirect=true&id="
						+ this.departement.getIdDepartement();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}*/
	}

	public String delete() {
		this.conversation.end();

		try {
			Departement deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Departement entities with pagination
	 */

	private int page;
	private long count;
	private List<Departement> pageItems;

	private Departement example = new Departement();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Departement getExample() {
		return this.example;
	}

	public void setExample(Departement example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Departement> root = countCriteria.from(Departement.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Departement> criteria = builder
				.createQuery(Departement.class);
		root = criteria.from(Departement.class);
		TypedQuery<Departement> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Departement> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String chefDepartement = this.example.getChefDepartement();
		if (chefDepartement != null && !"".equals(chefDepartement)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("chefDepartement")),
					'%' + chefDepartement.toLowerCase() + '%'));
		}
		String descriptionActiviteDepartement = this.example
				.getDescriptionActiviteDepartement();
		if (descriptionActiviteDepartement != null
				&& !"".equals(descriptionActiviteDepartement)) {
			predicatesList.add(builder.like(builder.lower(root
					.<String> get("descriptionActiviteDepartement")),
					'%' + descriptionActiviteDepartement.toLowerCase() + '%'));
		}
		String libelleDepartemnt = this.example.getLibelleDepartemnt();
		if (libelleDepartemnt != null && !"".equals(libelleDepartemnt)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleDepartemnt")),
					'%' + libelleDepartemnt.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Departement> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Departement entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<Departement> getAll() {

		CriteriaQuery<Departement> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Departement.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Departement.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final DepartementBean ejbProxy = this.sessionContext
				.getBusinessObject(DepartementBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Departement) value).getIdDepartement());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Departement add = new Departement();

	public Departement getAdd() {
		return this.add;
	}

	public Departement getAdded() {
		Departement added = this.add;
		this.add = new Departement();
		return added;
	}
}
