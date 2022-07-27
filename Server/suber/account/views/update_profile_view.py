from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger, Driver


class UpdateProfileView(APIView):

    def put(self, request):
        data = request.data
        try:
            username = data['username']
            new_username = data['new_username']
            firstname = data['firstname']
            lastname = data['lastname']
            user_type = data['user_type']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        if user_type == 'P':
            user_model = Passenger
        elif user_type == 'D':
            user_model = Driver
        else:
            raise ValidationError('Wrong user_type')

        user = get_object_or_404(user_model, username=username)

        user.username = new_username
        user.first_name = firstname
        user.last_name = lastname
        user.save()

        return Response('ok')
