from django.shortcuts import render_to_response, render
from GamesApp.models import Game, Players, Developer, Score, GameSave
from GamesApp.forms import GameForm, PaymentForm
from django.http import HttpResponse, HttpResponseRedirect, Http404
from django.core.context_processors import csrf
from django.utils import timezone
from django.conf import settings
from haystack.query import SearchQuerySet
from django.template import RequestContext
from django.contrib import messages
import logging
import watson
import string, random
from decimal import *
from django.contrib.auth.decorators import login_required
from payment.models import Payment
from hashlib import md5

logr = logging.getLogger(__name__)

def get_all_games(request):
    # language = 'en-gb'
    # session_language = 'en-gb'
    #
    # if 'lang' in request.COOKIES:
    #     language = request.COOKIES['lang']
    #
    # if 'lang' in request.session:
    #     session_language = request.session['lang']
    args = {}
    args.update(csrf(request))

    args['games'] = Game.objects.all()
    # args['language'] = language
    # args['session_language'] = session_language

    return render_to_response('games.html', args)


def get_game_by_id(request, game_id=1):
    is_developer=False
    if not  (request.user.is_anonymous()):
        user = request.user
        if Developer.objects.filter(developed_games=Game.objects.get(id=game_id),user=user):
            is_developer = True
        #purchased_games =
        games_purchased= Game.objects.get(id=game_id).players.count()
    return render(request, 'game.html',
                  {'game': Game.objects.get(id=game_id) , 'Developer':is_developer})
    




@login_required
def my_games(request):

        args = {}
        args.update(csrf(request))
        user = request.user
        playerobj, playerobjcreated = Players.objects.get_or_create(user=user)     # helpful when login through 3rd party, the developer is player by default
        #args['games'] = Game.objects.all()
        if playerobjcreated:
        # will not happen the code is edited so that the player or devloper are created during registratoin
        # see GameSite views user_registration
            args['games']=playerobj.game.all()
            return render_to_response('mygames.html', args)
        else:
            args['games']=playerobj.game.all()
            return render_to_response('mygames.html', args)
            #playerobj.game.add(Game.objects.get(id=game_id))
            #playerobj.save()

@login_required
def my_developed_games(request):
    user = request.user
    if Developer.objects.filter(user=user):
        args = {}
        args.update(csrf(request))
        playerobj = Developer.objects.get(user=user)
        #args['games'] = Game.objects.all()
        # if playerobjcreated:
        # # will not happen the code is edited so that the player or devloper are created during registratoin
        # # see GameSite views user_registration
        #     args['games']=playerobj.game.all()
        #     return render_to_response('mydevelopedgames.html', args)
        # else:
        args['games']=playerobj.developed_games.all()
        return render_to_response('mydevelopedgames.html', args)
    else:
        return HttpResponseRedirect('/games/all')   # todo sorry you are player not a developer

# def language(request, language='en-gb'): #  understanding some concepts about cookies and session
#     response = HttpResponse("setting language to %s" % language)
#
#     response.set_cookie('lang', language)
#
#     request.session['lang'] = language
#
#     return response

#todo define edit page with delete button
#todo add developed game tab
#todo add how many games purchased

@login_required
def create_game(request):
    user = request.user
    if Developer.objects.filter(user=user):
        if request.POST:
            form = GameForm(request.POST, request.FILES)
            #  request.FILES to know from where to get the file
            if form.is_valid():
                a = form.save()
                d=Developer.objects.get(user=user)
                # print (request.POST['game_name'])
                d.developed_games.add(Game.objects.get(game_name=request.POST['game_name']))
                d.save()
                return HttpResponseRedirect('/games/all')
        else:
            form = GameForm()

        args = {}
        args.update(csrf(request))

        args['form'] = form

        return render_to_response('create_game.html', args)
    else:
        return HttpResponseRedirect('/games/all') # todo inform the user that he is not developer

@login_required
def delete_game(request, game_id):
    user=request.user
    devlopobj=Developer.objects.get(user=user)
    if Game.objects.get(id=game_id) in  devlopobj.developed_games.all():
        Game.objects.get(id=game_id).delete()
        #messages.add_message(request,settings.DELETE_MESSAGE,"Game was deleted")
        return HttpResponseRedirect("/games/all")
    else:
        return HttpResponseRedirect("/games/all")

@login_required
def edit_game(request, game_id):
    gameobject=Game.objects.get(id=game_id)
    user = request.user
    if Developer.objects.filter(user=user):
        developerobj=Developer.objects.get(user=user)
        if Game.objects.get(id=game_id) in  developerobj.developed_games.all():
            if request.POST:
                print (request.POST)
                form = GameForm(request.POST, request.FILES)
                #  request.FILES to know from where to get the file
                if form.is_valid():
                    if gameobject.game_name==form['game_name'].value() and gameobject.game_name==form['game_name'].value() \
                        and gameobject.price== form['price'].value() and gameobject.code==form['code'].value() \
                        and gameobject.image==form['image'].value() and gameobject.image==form['image'].value() \
                        and gameobject.description==form['description'].value() :
                            pass
                    else:
                        gameobject.game_name=form['game_name'].value()
                        gameobject.game_name=form['game_name'].value()
                        gameobject.price= form['price'].value()
                        gameobject.code==form['code'].value()
                        gameobject.image=form['image'].value()
                        gameobject.image=form['image'].value()
                        gameobject.description=form['description'].value()
                        gameobject.save()
                    #form.save()

                    #d=Developer.objects.get(user=user)
                    # print (request.POST['game_name'])
                    #d.developed_games.add(Game.objects.get(game_name=request.POST['game_name']))
                    #d.save()
                    #Game.objects.get(id=game_id).delete() # todo error when no change
                return HttpResponseRedirect('/games/all')
            else:
                form = GameForm(instance=Game.objects.get(id=game_id))
                args = {}
                args.update(csrf(request))
                args['form'] = form
                args['game_id']= game_id
                return render_to_response('edit_game.html', args)
        else:
            return HttpResponseRedirect('/games/all') # todo inform the user that he is not developer
    else:
            return HttpResponseRedirect('/games/all') # todo inform the user that he is not developer
@login_required
def show_salehistory(request, game_id):
    user = request.user
    if Developer.objects.filter(user=user):
        theGame = Game.objects.get(id=game_id)
        payments = theGame.gamepayments.filter(status='S').order_by('-created_on')
        return render(request, 'salehistory.html', {'game': theGame, 'payments': payments})
    else:
        return HttpResponseRedirect('/games/get/'+game_id)
@login_required
def play_game(request, game_id):
    
    user = request.user
    playerobj, playerobjcreated = Players.objects.get_or_create(user=user)
    # helpful when login through 3rd party, the developer is player by default
    
    if request.POST:
        msgtype = request.POST['msgtype']
        #print ("msgtype: "+msgtype)
        if msgtype == "SCORE":
            scoreobj = Score.objects.create(game=Game.objects.get(id=game_id), name=playerobj, score=Decimal(request.POST['score']))
            scoreobj.save()
            return render(request, 'playgame.html',{'game': Game.objects.get(id=game_id), 'gameState': '' })
        elif msgtype == "SAVE":
            # Only 1 save file per game per account
            gameSaveobj, gameSaveobjcreated = GameSave.objects.get_or_create(game=Game.objects.get(id=game_id), player=playerobj)
            gameSaveobj.gameState = request.POST['gamestate']
            gameSaveobj.save()
            return render(request, 'playgame.html',{'game': Game.objects.get(id=game_id), 'gameState': request.POST['gamestate'] })
        elif msgtype == "LOAD_REQUEST":
            query_gameSaveobjs = playerobj.playersaves.filter(game=Game.objects.get(id=game_id))
            if (query_gameSaveobjs.exists()): # Did we find the savefile?
                gameSaveobj = query_gameSaveobjs[0]
            else: # savefile was not found
                return render(request, 'playgame.html', {'game': Game.objects.get(id=game_id), 'gameState': '', 'serviceMessage': 'No savefile for this game was found.'})
            return render(request, 'playgame.html', {'game': Game.objects.get(id=game_id), 'gameState': gameSaveobj.gameState})
    
    else: 
        if playerobjcreated:
    
            return HttpResponseRedirect("/games/all")
        #todo message you haven't purchased the game
    
        else:
    
            if Game.objects.get(id=game_id) in  playerobj.game.all():
                return render(request, 'playgame.html',{'game': Game.objects.get(id=game_id), 'gameState': '' })
            else :
                return HttpResponseRedirect("/games/all") # todo inform the user that he didn't buy the game
                #todo buy the game
                


def show_highscore(request, game_id):
    
    theGame = Game.objects.get(id=game_id)
    theScores = theGame.gamescores.order_by('-score')[:10]
    
    return render(request, 'highscores.html', {'scores': theScores})

############################All the methods below are not working######################################
############################still unable to figure out the problem######################################
############################three diffrent ways, Ajax, haystack, watson######################################

def search_description(request):  #  SearchQuerySet is defined in search_indexes file  **Ala** # not working

    games = SearchQuerySet().autocomplete(content_auto=request.POST.get('search_text', ''))
    return render_to_response('searchresult.html', {'games': games})

def search_watson(request):

    games = watson.search("request.POST.get('search_text', '')")#request.POST.get('search_text', '')
    print (request.POST.get('search_text', ''))
    print (games)
    for game in games:
        print (game.game_name)
        print (game.description)
    return render_to_response('searchresult.html', {'games': games})

#
# def search_ajax(request): # not wroking
#     args = {}
#     args.update(csrf(request))
#     if request.method == 'POST':
#         search_text =request.POST['search_text']
#         print (search_text)
#     else:
#         search_text = ''
#     games= Game.objects.filter(game_name__contians=search_text)
#     print (search_text)
#     for game in games:
#         print (game.game_name)
#         print (game.description)
#     return HttpResponseRedirect("/games/")
#     #return render(request, 'searchresult.html',{'games' : games})
def search(request): # not wroking
    search_text = request.GET.get('q', '')
    games= Game.objects.filter(game_name__contains=search_text) | Game.objects.filter(description__contains=search_text)
    return render(request, 'searchresult.html',{'games': games})


   #
   #  args = {}
   # # args.update(csrf(request))
   #  if request.method == 'POST':
   #      search_text =request.POST['search_text']
   #      print (search_text)
   #  else:
   #      search_text = ''
   #  games= Game.objects.filter(game_name__contians=search_text)
   #  print (search_text)
   #  for game in games:
   #      print (game.game_name)
   #      print (game.description)
   #  return HttpResponseRedirect("/games/")
   #  #return render(request, 'searchresult.html',{'games' : games})
