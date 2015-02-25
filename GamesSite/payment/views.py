from django.shortcuts import render
from django.shortcuts import render_to_response, render
from GamesApp.models import Game, Players, Developer, Score, GameSave
from payment.models import Payment
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
from hashlib import md5

def generatePID(size=10, chars=string.ascii_letters+string.digits):
    '''
        Generates random unique PID
    '''
    pid = ''.join(random.choice(chars) for _ in range(size))
    while Payment.objects.filter(pid=pid):
        pid = ''.join(random.choice(chars) for _ in range(size))
    return pid

def checksums(generate, pid, **params):
        secret_key = "34d39121ad0e786de3112b880acb29a3"
        
        if generate:
            price = params.get("price")
            sid = params.get("sid")
            checksumstr = "pid=%s&sid=%s&amount=%s&token=%s"%(pid, sid, price, secret_key)
        else:
            ref = params.get("ref")
            checksumstr = "pid=%s&ref=%s&token=%s"%(pid, ref, secret_key)
            
        m = md5(checksumstr.encode("ascii"))
        checksum = m.hexdigest()
        return checksum

@login_required
def buy_game(request, game_id=1):
        # TODO: Delete useless payment objects if user doesn't press 'Accept Payment' or change payment creation to later part (when posting the form)
    

        sid = "eafh20MENOwqdn"
        #secret_key = "34d39121ad0e786de3112b880acb29a3"
        pid = generatePID()

        theGame = Game.objects.get(id=game_id)
        gameName = theGame.game_name
        price = str(theGame.price)
        checksum = checksums(True, pid, price=price, sid=sid)

        user = request.user
        print (user)
        print (type((user)))
        playerobj, playerobjcreated = Players.objects.get_or_create(user=user)
        print (playerobj)
        print (playerobjcreated)
        if not Players.objects.filter(game=theGame, user=user):
            # adding payment information and will redirect forward as the user didn't have the game already
            paymentobj, paymentobjcreated = Payment.objects.get_or_create(pid=pid, user=user, game=theGame, amount=price, status='P')
            paymentobj.save()
        else:
            # just renders the same page again because the user has bought the game already
            return HttpResponseRedirect('/games/mygames')
        '''
        if playerobjcreated:
            playerobj.game.add(Game.objects.get(id=game_id))
            playerobj.save()
        else:
            if not Players.objects.filter(game=Game.objects.get(id=game_id),user=user): 
                playerobj.game.add(Game.objects.get(id=game_id))
                playerobj.save()
        '''
        return render(request, 'payment.html', {'game': theGame, 'pid': pid, 'sid': sid, 'amount': price, 'checksum': checksum, 'gameName': gameName})

def buy_success(request, game_id=1):
    #user = request.User
    #playerobj, playerobjcreated = Players.objects.get_or_create(user=user)
    
    if request.GET:
        pid = request.GET['pid']
        ref = request.GET['ref']
        checksum = request.GET['checksum']
        checksum_check = checksums(False, pid, ref=ref)
        # Checksum ok?
        if checksum_check != checksum:
            # Checksum doesn't match, redirecting elsewhere
            print ("checksum didn't match: "+checksum+" | "+checksum_check)
            return HttpResponseRedirect('/games/all')
        else:
            # Checksum is okay, continue
            paymentobj = Payment.objects.get(pk=pid)
            print ("paymentobj: "+str(paymentobj))
            playerobj, playerobjcreated = playerobj, playerobjcreated = Players.objects.get_or_create(user=paymentobj.user)
            if paymentobj.status != 'P':
                # Status was not pending so the payment has already been handled
                return HttpResponseRedirect('/games/all')
            theGame = paymentobj.game
            playerobj.game.add(Game.objects.get(id=theGame.id))
            paymentobj.status = 'S'
            paymentobj.save()
            playerobj.save()
            return render(request, 'payment_success.html', {'gameName': theGame.game_name})
    else:
        raise Http404
    
    '''
    theGame = Game.objects.get(id=game_id)
    gameName = theGame.game_name
    gameID = theGame.id
    print("buy_success -- game_id: "+game_id)
    
    if playerobjcreated:
        pass
    else:
        if not Players.objects.filter(game=Game.objects.get(id=game_id),user=user): 
            playerobj.game.add(Game.objects.get(id=game_id))
            playerobj.save()
    return render(request, 'payment_success.html', {'gameName': gameName, 'gameID': gameID})
    '''

def buy_cancel_or_fail(request):
    if request.GET:
        pid = request.GET['pid']
        ref = request.GET['ref']
        checksum = request.GET['checksum']
        checksum_check = checksums(False, pid, ref=ref)
        # Checksum ok?
        if checksum_check != checksum:
            # Checksum doesn't match, redirecting elsewhere
            print ("checksum didn't match: "+checksum+" | "+checksum_check)
            return HttpResponseRedirect('/games/all')
        else:
            # Checksum is okay, continue
            paymentobj = Payment.objects.get(pk=pid)
            print ("paymentobj: "+str(paymentobj))
            if paymentobj.status != 'P':
                # Status was not pending so the payment has already been handled
                return HttpResponseRedirect('/games/all')
            theGame = paymentobj.game
            # Alternatively could just remove the payment object completely
            # But probably good to have for logging purposes for customer support or something similar
            if '/payment/cancel/' in request.path: # if cancel event
                paymentobj.status = 'C'
                paymentobj.save()
                return render(request, 'payment_cancel.html', {'gameName': theGame.game_name})
            elif '/payment/fail/' in request.path: # if fail/error event
                paymentobj.status = 'E'
                paymentobj.save()
                return render(request, 'payment_fail.html', {'gameName': theGame.game_name})
    else:
        raise Http404
# Create your views here.
