from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError, NotAcceptable
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger
from discount.models import DiscountCode
from trip.models import OnGoingTrip

PRICE = 30000


class SearchForDriverView(APIView):
    def post(self, request):
        data = request.data
        try:
            username = data['username']
            source = data['source']
            destination = data['destination']
            discount_code = data['discount_code']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._search(username, source, destination, discount_code)

        return Response('ok')

    def _search(self, username, source, destination, discount_code):
        passenger = get_object_or_404(Passenger, username=username)

        final_price = self._calculate_price(discount_code)

        if passenger.balance < final_price:
            raise NotAcceptable('Your balance is lower than trip price')

        OnGoingTrip.objects.filter(passenger=passenger).delete()

        OnGoingTrip.objects.create(
            passenger=passenger,
            source=source,
            destination=destination,
            price=final_price
        )

    def _calculate_price(self, discount_code):
        if discount_code:
            discount = get_object_or_404(DiscountCode, code=discount_code)
            return PRICE - (PRICE * discount.percent // 100)

        return PRICE
