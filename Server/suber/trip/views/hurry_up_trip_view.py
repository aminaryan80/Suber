from django.utils.datastructures import MultiValueDictKeyError
from rest_framework.exceptions import ValidationError, NotAcceptable
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework.views import APIView

from account.models import Passenger
from discount.models import DiscountCode
from trip.models import OnGoingTrip


class SearchForDriverView(APIView):

    def put(self, request):
        data = request.data
        try:
            username = data['username']
            source = data['source']
            destination = data['destination']
            discount_code = data['discount_code']
            price = data['price']
        except MultiValueDictKeyError:
            raise ValidationError('Some data is missing!')

        self._search(username, source, destination, discount_code, price)

        return Response('ok')

    def _search(self, username, source, destination, discount_code, price):
        passenger = get_object_or_404(Passenger, username=username)

        final_price = self._calculate_price(price, discount_code)

        if passenger.balance < final_price:
            raise NotAcceptable('Your balance is lower than trip price')

        OnGoingTrip.objects.filter(username=username).delete()

        OnGoingTrip.objects.create(
            passenger=passenger,
            source=source,
            destination=destination,
            price=final_price
        )

    def _calculate_price(self, price, discount_code):
        if discount_code:
            discount = get_object_or_404(DiscountCode, code=discount_code)
            return price - (price * discount.percent // 100)

        return price
