from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('account/', include('account.urls')),
    path('trip/', include('trip.urls')),
    path('discount/', include('discount.urls')),
]
