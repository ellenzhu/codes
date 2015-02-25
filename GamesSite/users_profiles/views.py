from django.shortcuts import render_to_response
from django.http import HttpResponseRedirect
from django.core.context_processors import csrf
from users_profiles.forms import UserProfileForm,UserProfileForm2
from django.contrib.auth.decorators import login_required
from users_profiles.models import UserProfile
#from GamesSite.forms import MyRegistrationForm
#todo Developer and Player are better defined in user profiles application?
@login_required
def user_profile(request):
    user=request.user
    if request.method == 'POST':
        form = UserProfileForm(request.POST, instance=request.user.profile)
        userform=UserProfileForm2(request.POST,instance=request.user)
        #  instance is to populate the form with user data from the request
        if form.is_valid() and userform.is_valid():
            form.save()
            userform.save()
            return HttpResponseRedirect('/accounts/logged_in')
    else:
        user = request.user
        profile = user.profile # defined in the model - funky way to add profile for the user
        # this will execute User.profile = property(lambda u: UserProfile.objects.get_or_create(user=u)[0]) in models
        userform=UserProfileForm2(instance=user)
        form = UserProfileForm(instance=profile)
        
    args = {}
    args.update(csrf(request))
    args['form'] = form
    args['userform'] = userform
    return render_to_response('profile.html', args)   

