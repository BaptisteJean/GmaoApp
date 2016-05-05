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

import com.thedevbridge.gmaoapp.model.Technicien;
import com.thedevbridge.gmaoapp.model.Departement;
import com.thedevbridge.gmaoapp.model.Equipe;
import com.thedevbridge.gmaoapp.model.Personnel;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.convert.ConverterException;

/**
 * Backing bean for Technicien entities.
 * <p/>
 * This class provides CRUD functionality for all Technicien entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TechnicienBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "GmaoApp-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	/*
	 * Support creating and retrieving Technicien entities
	 */

	private Long id;
	private Technicien technicien;
        private Departement departement;
        private Equipe equipe;
        private Personnel personnel;
        
        private boolean showTable = false;
        private List<Technicien> allTechnicien;
        private List<Departement> allDepartement;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Technicien getTechnicien() {
		return this.technicien;
	}

	public void setTechnicien(Technicien technicien) {
		this.technicien = technicien;
	}
        
        public Departement getDepartement() {
		return this.departement;
	}

	public void setDepartement(Departement departement) {
		this.departement = departement;
	}
        
        public Equipe getEquipe() {
		return this.equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
        
        public Personnel getPersonnel() {
		return this.personnel;
	}

	public void setPersonnel(Personnel personnel) {
		this.personnel = personnel;
	}
        
        public boolean getShowTable(){
            return this.showTable;
        }

        public void setShowTable(boolean showTable){
            this.showTable = showTable;
        }
        
        public List<Technicien> getAllTechnicien(){
            return this.allTechnicien;
        }
        
        public void setAllTechnicien(List<Technicien> allTechnicien){
            this.allTechnicien = allTechnicien;
        }
        
        public List<Departement> getAllDepartement(){
            return this.allDepartement;
        }
        
        public void setAllDepartement(List<Departement> allDepartement){
            this.allDepartement = allDepartement;
        }
        
        @PostConstruct
        public void loadTechnicien(){
            allTechnicien = (List<Technicien>)entityManager.createNamedQuery("Technicien.findAll").getResultList();
            allDepartement = (List<Departement>)entityManager.createNamedQuery("Departement.findAll").getResultList();
            
            if(allTechnicien.isEmpty()){
                this.showTable = false;
            }else{
                this.showTable = true;
            }
        }

	@Inject
	private Conversation conversation;

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
			this.technicien = this.example;
		} else {
			this.technicien = findById(getId());
		}
	}

	public Technicien findById(Long id) {

		return this.entityManager.find(Technicien.class, id);
	}

	/*
	 * Support updating and deleting Technicien entities
	 */

	public String update() {
		this.conversation.end();
                
                Random random = new Random();
                
                //departement = technicien.getIdDepartement();
                equipe = technicien.getIdEquipe();
                personnel = technicien.getIdPersonnel();
                
                //departement.setIdDepartement(random.nextLong());
                equipe.setIdEquipe(random.nextLong());
                //equipe.setIdDepartemant(departement);
                personnel.setIdPersonnel(random.nextLong());
                
                //this.entityManager.persist(departement);
                //List<Departement> departementSaved = (List<Departement>)entityManager.createNamedQuery("Departement.findByIdDepartement").setParameter("idDepartement", departement.getIdDepartement()).getResultList();
                //equipe.setIdDepartemant(departementSaved.get(departementSaved.size() - 1));
                equipe.setIdDepartemant(this.technicien.getIdDepartement());
                
                this.entityManager.persist(equipe);
                this.entityManager.persist(personnel);
                
                //technicien.setIdDepartement(departementSaved.get(departementSaved.size() - 1));
                //technicien.setIdDepartement(this.technicien.getIdDepartement());
                List<Equipe> equipeSaved = (List<Equipe>)entityManager.createNamedQuery("Equipe.findByIdEquipe").setParameter("idEquipe", equipe.getIdEquipe()).getResultList();
                technicien.setIdEquipe(equipeSaved.get(equipeSaved.size() - 1));
                List<Personnel> personnelSaved = (List<Personnel>)entityManager.createNamedQuery("Personnel.findByIdPersonnel").setParameter("idPersonnel", personnel.getIdPersonnel()).getResultList();
                technicien.setIdPersonnel(personnelSaved.get(personnelSaved.size() - 1));
                
                technicien.setIdTechnicien(random.nextLong());
                this.entityManager.persist(technicien);
                
                String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
                return viewId + "?faces-redirect=true";

		/*try {
			if (this.id == null) {
				this.entityManager.persist(this.technicien);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.technicien);
				return "view?faces-redirect=true&id="
						+ this.technicien.getIdTechnicien();
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
			Technicien deletableEntity = findById(getId());
			Departement idDepartement = deletableEntity.getIdDepartement();
			idDepartement.getTechnicienList().remove(deletableEntity);
			deletableEntity.setIdDepartement(null);
			this.entityManager.merge(idDepartement);
			Equipe idEquipe = deletableEntity.getIdEquipe();
			idEquipe.getTechnicienList().remove(deletableEntity);
			deletableEntity.setIdEquipe(null);
			this.entityManager.merge(idEquipe);
			Personnel idPersonnel = deletableEntity.getIdPersonnel();
			idPersonnel.getTechnicienList().remove(deletableEntity);
			deletableEntity.setIdPersonnel(null);
			this.entityManager.merge(idPersonnel);
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
	 * Support searching Technicien entities with pagination
	 */

	private int page;
	private long count;
	private List<Technicien> pageItems;

	private Technicien example = new Technicien();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Technicien getExample() {
		return this.example;
	}

	public void setExample(Technicien example) {
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
		Root<Technicien> root = countCriteria.from(Technicien.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Technicien> criteria = builder
				.createQuery(Technicien.class);
		root = criteria.from(Technicien.class);
		TypedQuery<Technicien> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Technicien> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String fonctionTechicien = this.example.getFonctionTechicien();
		if (fonctionTechicien != null && !"".equals(fonctionTechicien)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("fonctionTechicien")),
					'%' + fonctionTechicien.toLowerCase() + '%'));
		}
		Departement idDepartement = this.example.getIdDepartement();
		if (idDepartement != null) {
			predicatesList.add(builder.equal(root.get("idDepartement"),
					idDepartement));
		}
		Equipe idEquipe = this.example.getIdEquipe();
		if (idEquipe != null) {
			predicatesList.add(builder.equal(root.get("idEquipe"), idEquipe));
		}
		Personnel idPersonnel = this.example.getIdPersonnel();
		if (idPersonnel != null) {
			predicatesList.add(builder.equal(root.get("idPersonnel"),
					idPersonnel));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Technicien> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Technicien entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Technicien> getAll() {

		CriteriaQuery<Technicien> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Technicien.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Technicien.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final TechnicienBean ejbProxy = this.sessionContext
				.getBusinessObject(TechnicienBean.class);
                    
                    System.out.println("Starting convertion");

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

				return String.valueOf(((Technicien) value).getIdTechnicien());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Technicien add = new Technicien();

	public Technicien getAdd() {
		return this.add;
	}

	public Technicien getAdded() {
		Technicien added = this.add;
		this.add = new Technicien();
		return added;
	}
}
