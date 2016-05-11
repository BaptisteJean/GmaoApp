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

import com.thedevbridge.gmaoapp.model.Equipe;
import com.thedevbridge.gmaoapp.model.Departement;
import com.thedevbridge.gmaoapp.model.Technicien;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;

/**
 * Backing bean for Equipe entities.
 * <p/>
 * This class provides CRUD functionality for all Equipe entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EquipeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Equipe entities
	 */

	private Long id;
	private Equipe equipe;
        private Departement departement;
        
        private boolean showTable = false;
        private List<Equipe> allEquipe;
        private List<Technicien> allTechnicien;
        private List<Departement> allDepartement;
        
        private Technicien chefEquipe;
        private Technicien technicien1;
        private Technicien technicien2;
        private Technicien technicien3;
        private Technicien technicien4;
        
        private String alert = "Desolé, ce technicien a déjà été choisi !";
        private boolean isChoised = true;
        

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Equipe getEquipe() {
		return this.equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}
        
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
        
        public List<Equipe> getAllEquipe(){
            return this.allEquipe;
        }
        
        public void setAllEquipe(List<Equipe> allEquipe){
            this.allEquipe = allEquipe;
        }
        
        public Technicien getChefEquipe() {
		return this.chefEquipe;
	}

	public void setChefEquipe(Technicien chefEquipe) {
		this.chefEquipe = chefEquipe;
	}
        
        public Technicien getTechnicien1() {
		return this.technicien1;
	}

	public void setTechnicien1(Technicien technicien1) {
		this.technicien1 = technicien1;
	}
        
        public Technicien getTechnicien2() {
		return this.technicien2;
	}

	public void setTechnicien2(Technicien technicien2) {
		this.technicien2 = technicien2;
	}
        
        public Technicien getTechnicien3() {
		return this.technicien3;
	}

	public void setTechnicien3(Technicien technicien3) {
		this.technicien3 = technicien3;
	}
        
        public Technicien getTechnicien4() {
		return this.technicien4;
	}

	public void setTechnicien4(Technicien technicien4) {
		this.technicien4 = technicien4;
	}
        
        public String getAlert() {
		return this.alert;
	}

	public void setId(String alert) {
		this.alert = alert;
	}
        
        public boolean getIsChoised() {
		return this.isChoised;
	}

	public void setId(boolean isChoised) {
		this.isChoised = isChoised;
	}
        
        @PostConstruct
        public void loadTechnicien(){
            allTechnicien = (List<Technicien>)entityManager.createNamedQuery("Technicien.findAll").getResultList();
            allDepartement = (List<Departement>)entityManager.createNamedQuery("Departement.findAll").getResultList();
            allEquipe = (List<Equipe>)entityManager.createNamedQuery("Equipe.findAll").getResultList();
            
            if(allEquipe.isEmpty()){
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
			this.equipe = this.example;
		} else {
			this.equipe = findById(getId());
		}
	}

	public Equipe findById(Long id) {

		return this.entityManager.find(Equipe.class, id);
	}

	/*
	 * Support updating and deleting Equipe entities
	 */

	public String update() {
		this.conversation.end();
                
                Random random = new Random();
                Technicien chefEquipeMerged = null;
                Technicien technicien1Merged = null;
                Technicien technicien2Merged = null;
                Technicien technicien3Merged = null;
                Technicien technicien4Merged = null;
                
                equipe.setIdEquipe(random.nextLong());
                //this.entityManager.persist(equipe);
                
                if(chefEquipe != null){
                    chefEquipe.setIdEquipe(equipe);
                    equipe.setChefEquipe(chefEquipe.getIdPersonnel().getNomPersonnel());
                    System.out.println("************   **************   ******* chef Ecquipe " + equipe.getChefEquipe());
                }
                if(technicien1 != null){
                    technicien1.setIdEquipe(equipe);
                }
                if(technicien2 != null){
                    technicien2.setIdEquipe(equipe);
                }
                if(technicien3 != null){
                    technicien3.setIdEquipe(equipe);
                }
                if(technicien4 != null){
                    technicien4.setIdEquipe(equipe);
                }
                
                this.entityManager.persist(equipe);
                
                if(chefEquipe != null){
                    chefEquipeMerged = this.entityManager.merge(chefEquipe);
                }
                if(technicien1 != null){
                    technicien1Merged = this.entityManager.merge(technicien1);
                }
                if(technicien2 != null){
                    technicien2Merged = this.entityManager.merge(technicien2);
                }
                if(technicien3 != null){
                    technicien3Merged = this.entityManager.merge(technicien3);
                }
                if(technicien4 != null){
                    technicien4Merged = this.entityManager.merge(technicien4);
                }
                //Technicien chefEquipeMerged = this.entityManager.merge(chefEquipe);
                //Technicien technicien1Merged = this.entityManager.merge(chefEquipe);
                //Technicien technicien2Merged = this.entityManager.merge(chefEquipe);
                //Technicien technicien3Merged = this.entityManager.merge(chefEquipe);
                //Technicien technicien4Merged = this.entityManager.merge(chefEquipe);
                
                equipe.getTechnicienList().add(chefEquipeMerged);
                equipe.getTechnicienList().add(technicien1Merged);
                equipe.getTechnicienList().add(technicien2Merged);
                equipe.getTechnicienList().add(technicien3Merged);
                equipe.getTechnicienList().add(technicien4Merged);
                Equipe equipeMerged = this.entityManager.merge(equipe);
                
                System.out.println("**** ***** ***** **** Chef Equipe merge with succes ");
                
                String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
                return viewId + "?faces-redirect=true";

		/*try {
			if (this.id == null) {
				this.entityManager.persist(this.equipe);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.equipe);
				return "view?faces-redirect=true&id="
						+ this.equipe.getIdEquipe();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}*/
	}
        
        public void testIfChoised(ValueChangeEvent e){
            //if((Technicien)e.getNewValue() != null){
                this.isChoised = false;
            //}
        }

	public String delete() {
		this.conversation.end();

		try {
			Equipe deletableEntity = findById(getId());
			Departement idDepartemant = deletableEntity.getIdDepartemant();
			idDepartemant.getEquipeList().remove(deletableEntity);
			deletableEntity.setIdDepartemant(null);
			this.entityManager.merge(idDepartemant);
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
	 * Support searching Equipe entities with pagination
	 */

	private int page;
	private long count;
	private List<Equipe> pageItems;

	private Equipe example = new Equipe();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Equipe getExample() {
		return this.example;
	}

	public void setExample(Equipe example) {
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
		Root<Equipe> root = countCriteria.from(Equipe.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Equipe> criteria = builder.createQuery(Equipe.class);
		root = criteria.from(Equipe.class);
		TypedQuery<Equipe> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Equipe> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String chefEquipe = this.example.getChefEquipe();
		if (chefEquipe != null && !"".equals(chefEquipe)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("chefEquipe")),
					'%' + chefEquipe.toLowerCase() + '%'));
		}
		String descriptionActiviteEquipe = this.example
				.getDescriptionActiviteEquipe();
		if (descriptionActiviteEquipe != null
				&& !"".equals(descriptionActiviteEquipe)) {
			predicatesList.add(builder.like(builder.lower(root
					.<String> get("descriptionActiviteEquipe")),
					'%' + descriptionActiviteEquipe.toLowerCase() + '%'));
		}
		String libelleEquipe = this.example.getLibelleEquipe();
		if (libelleEquipe != null && !"".equals(libelleEquipe)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleEquipe")),
					'%' + libelleEquipe.toLowerCase() + '%'));
		}
		Departement idDepartemant = this.example.getIdDepartemant();
		if (idDepartemant != null) {
			predicatesList.add(builder.equal(root.get("idDepartemant"),
					idDepartemant));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Equipe> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Equipe entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Equipe> getAll() {

		CriteriaQuery<Equipe> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Equipe.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Equipe.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EquipeBean ejbProxy = this.sessionContext
				.getBusinessObject(EquipeBean.class);

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

				return String.valueOf(((Equipe) value).getIdEquipe());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Equipe add = new Equipe();

	public Equipe getAdd() {
		return this.add;
	}

	public Equipe getAdded() {
		Equipe added = this.add;
		this.add = new Equipe();
		return added;
	}
}
