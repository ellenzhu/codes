__author__ = 'alabrazi'
from django.shortcuts import render_to_response, render
from django.http import HttpResponseRedirect
from django.contrib import auth
from django.core.context_processors import csrf

from GamesSite.forms import UserForm, MyRegistrationForm
from GamesSite.forms import UserForm
from users_profiles.forms import ShortUserProfileForm
from django.contrib.auth.decorators import login_required
from GamesApp.models import Players, Developer

def register_user(request):

    if request.method == 'POST':
        form = MyRegistrationForm(request.POST)
        if form.is_valid():
            form.save()
            return HttpResponseRedirect('/accounts/successful_registration')

    else:
        form = MyRegistrationForm()
    args = {}
    args.update(csrf(request))

    args['form'] = form

    return render_to_response('register1.html', args)

# def user_registration(request):# messing up the password, after regestration it was not possible to login
#
#     registered = False
#     if request.method == 'POST':
#         user_form = UserForm(data=request.POST)
#         profile_form = ShortUserProfileForm(data=request.POST)
#         if user_form.is_valid() and profile_form.is_valid():
#             user = user_form.save()
#             user.set_password(user.password)
#             user.save()
#             # Since we need to set the user attribute ourselves, we set commit=False.
#             # This delays saving the model until we're ready to avoid integrity problems.
#             profile = profile_form.save(commit=False)
#             profile.user = user
#             if 'picture' in request.FILES:
#                 profile.picture = request.FILES['picture']
#             profile.save()
#             registered = True
#
#         # Invalid form or forms - mistakes or something else?
#         # Print problems to the terminal.
#         # They'll also be shown to the user.
#         else:
#             print (user_form.errors, profile_form.errors)
#
#     else:
#         user_form = UserForm()
#         profile_form = ShortUserProfileForm()
#
#     # Render the template depending on the context.
#     return render(request,'register2.html', {'user_form': user_form, 'profile_form': profile_form, 'registered': registered} )

def user_registration(request):

    registered = False
    if request.method == 'POST':
        user_form = UserForm(request.POST)
        profile_form = ShortUserProfileForm(request.POST) # This is defined in user profile app to bring category into registration
        if user_form.is_valid() and profile_form.is_valid():
            user_form.save()
            #user.set_password(user.password)
            #user.save()
            # Since we need to set the user attribute ourselves, we set commit=False.
            # This delays saving the model until we're ready to avoid integrity problems.
            profile = profile_form.save(commit=False)
            profile.user = user_form.save()
            if 'picture' in request.FILES:
                profile.picture = request.FILES['picture']
            profile.save()
            if profile.category == 'Developer' :
                d = Developer(user=profile.user)
                d.save()
            else:
                d = Players(user=profile.user)
                d.save()

            registered = True

        # Invalid form or forms - mistakes or something else?
        # Print problems to the terminal.
        # They'll also be shown to the user.
        else:
            print (user_form.errors, profile_form.errors)

    else:
        user_form = UserForm()
        profile_form = ShortUserProfileForm()

    # Render the template depending on the context.
    return render(request,'register2.html', {'user_form': user_form, 'profile_form': profile_form, 'registered': registered} )

def successful_registration(request):

    return render_to_response('successful_registration.html')


def login(request):

    entered_data = {}
    entered_data.update(csrf(request))
    return render_to_response('login.html', entered_data)


def logged_in(request):

    return render_to_response('logged_in.html', {'full_name': request.user.username})


def invalid(request):

    return render_to_response('invalid_login.html')

@login_required
def logout(request):

    auth.logout(request)
    return render_to_response('logout.html')

def user_authentication(request):

    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    user = auth.authenticate(username=username, password=password)
    print (username)
    print (password)
    if user is not None:
        auth.login(request, user)
        return HttpResponseRedirect('/accounts/logged_in')
    else:
        return HttpResponseRedirect('/accounts/invalid')