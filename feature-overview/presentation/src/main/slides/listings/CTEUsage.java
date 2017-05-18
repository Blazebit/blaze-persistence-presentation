EntityViewSetting<CatView, CriteriaBuilder<CatView>> catSetting = EntityViewSetting.create(CatView.class);
List<CatView> catHierarchy = getCatHierarchy(1, catSetting);