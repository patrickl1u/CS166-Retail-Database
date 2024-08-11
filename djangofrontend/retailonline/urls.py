from django.urls import path
from . import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path("", views.index, name = "index"),
    path("store/", views.store_overview, name = "store_overview"),
    path("store/<int:pk>/", views.store_detail, name = "store_detail"),
    path("store/<int:pk>/products", views.store_products, name = "store_products"),
    path("user/<int:pk>/", views.user_detail, name = "user_detail"),
]
urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
