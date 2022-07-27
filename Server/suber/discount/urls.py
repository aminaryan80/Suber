from django.urls import path

from discount.views.get_discount_codes import GetDiscountCodesView
from trip.views.accept_trip_by_driver_view import AcceptTripByDriverView
from trip.views.get_ongoing_trip_status_view import GetOngoingTripStatusView
from trip.views.get_ongoing_trips import GetOngoingTripsView
from trip.views.hurry_up_trip_view import HurryUpTripView
from trip.views.search_for_driver_view import SearchForDriverView
from trip.views.trip_history_view import TripHistoryView

urlpatterns = [
    path('get-discount-codes/', GetDiscountCodesView.as_view(), name='get-discount-codes'),
]
