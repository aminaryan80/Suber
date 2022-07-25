from django.http import JsonResponse
from rest_framework.views import APIView

from trip.models import OnGoingTrip


class GetOngoingTripsView(APIView):
    def get(self, request):
        data = list(OnGoingTrip.objects.values(
            'id',
            'source',
            'destination',
            'price'
        ))
        print(data)
        return JsonResponse(data, safe=False)
