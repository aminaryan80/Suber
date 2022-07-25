from django.urls import path

from account.views.get_profile_view import GetProfileView
from account.views.login_view import LoginView
from account.views.register_view import RegisterView

urlpatterns = [
    path('login/', LoginView.as_view(), name='login'),
    path('register/', RegisterView.as_view(), name='register'),
    path('get-profile/', GetProfileView.as_view(), name='get-profile'),
]
