from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.views import APIView

from account.models import Passenger


class LoginView(APIView):

    def post(self, request):
        data = request.data
        try:
            username = data['username']
            password = data['password']
            user_type = data['user_type']
        except MultiValueDictKeyError:
            return ValidationError('Username or Password is missing!')

        return self._login(username, password, user_type)

    def _login(self, username, password, user_type):
        if user_type == 'P':
            return self._login_passenger(username, password)
        elif user_type == 'D':
            return self._login_driver(username, password)
        else:
            return ValidationError('Wrong user_type!')

    def _login_passenger(self, username, password):
        try:
            user = Passenger.objects.get(username=username)
            assert user.password == password
        except Exception:
            return ValidationError('Wrong username or password')

    def _login_driver(self, username, password):
        try:
            user = Passenger.objects.get(username=username)
            assert user.password == password
        except Exception:
            return ValidationError('Wrong username or password')
