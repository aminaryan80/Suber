from django.urls import path

from trip.views.accept_trip_by_driver_view import AcceptTripByDriverView
from trip.views.get_ongoing_trip_status_view import GetOngoingTripStatusView
from trip.views.get_ongoing_trips import GetOngoingTripsView
from trip.views.hurry_up_trip_view import HurryUpTripView
from trip.views.search_for_driver_view import SearchForDriverView
from trip.views.trip_history_view import TripHistoryView

urlpatterns = [
    path('trip-history/', TripHistoryView.as_view(), name='trip-history'),
    path('search-for-driver/', SearchForDriverView.as_view(), name='search-for-driver'),
    path('hurry-up/', HurryUpTripView.as_view(), name='hurry-up'),
    path('get-ongoing-trips/', GetOngoingTripsView.as_view(), name='get-ongoing-trips'),
    path('accept-trip/', AcceptTripByDriverView.as_view(), name='accept-trip'),
    path('get-trip-status/', GetOngoingTripStatusView.as_view(), name='get-trip-status'),
]
