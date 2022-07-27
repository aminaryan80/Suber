from django.urls import path

from account.views.charge_balance_passenger_view import ChargeBalancePassengerView
from account.views.get_profile_view import GetProfileView
from account.views.login_view import LoginView
from account.views.register_view import RegisterView
from account.views.update_profile_view import UpdateProfileView

urlpatterns = [
    path('login/', LoginView.as_view(), name='login'),
    path('register/', RegisterView.as_view(), name='register'),
    path('get-profile/', GetProfileView.as_view(), name='get-profile'),
    path('update-profile/', UpdateProfileView.as_view(), name='update-profile'),
    path('charge-account/', ChargeBalancePassengerView.as_view(), name='charge-account'),
]
