#!env python

import logging
import sys
import os
import random
import time

logging.basicConfig(format='%(levelname)s:  %(message)s', level=logging.DEBUG)
logger = logging.getLogger(__file__)

#logging.info("loading %s" % (__file__))

board_height = 15
board_width = 15
length_to_win = 5

turn_length_in_seconds = 10

move_file_name = "move_file"


class GomokuBoard(object):
    class _SingleField(object):
        isEmpty = True
        team = None

        def playField(self, team):
            result = False
            self.isEmpty = False
            self.team = team
            result = True
            return result

    width = None
    height = None

    def __init__(self, width=8, height=8):
        super(GomokuBoard, self).__init__()
        self.width = width
        self.height = height
        self._field = [[self._SingleField() for y in range(height)]
                       for x in range(width)]

        self.init_field() #originally used to init minefield, but not really useful here...

    def init_field(self):
        pass

    def __getitem__(self, index):
        x, y = index
        return self._field[x][y]

    def isFieldOpen(self, (x,y)):
        return self._field[x][y].isEmpty

    def placeToken(self, move):
        return self._field[move.x][move.y].playField(move.team_name)

    def getBoard(self):
        board = [[self._field[x][y].team for y in range(self.height)]
                 for x in range(self.width)]

        return board

    def isFull(self):
        for x in range(self.width):
            for y in range(self.height):
                if self.isFieldOpen( (x, y) ):
                    return False
        return True

class Move(object):
    def __init__(self, team_name, x_loc, y_loc):
        self.team_name = team_name
        self.x = x_loc
        self.y = y_loc

    def __str__(self):
        return "%s %s %s" % (self.team_name, chr(self.x + ord('a')), self.y)

class Game(object):
    def __init__(self, size_x=board_width, size_y=board_height, length_to_win=length_to_win):
        self.turn = 0
        self.board = GomokuBoard(size_x, size_y)
        self.length_to_win = length_to_win

    def isMoveUnique(self, move):
        logging.debug("unique: %s" % (self.board.isFieldOpen( (move.x, move.y) )))
        return self.board.isFieldOpen( (move.x, move.y) )

    def isMoveOnBoard(self, move):
        logging.debug("in x: %s" % ( 0 <= move.x and move.x < self.board.width ))
        logging.debug("on board: %s" % (((move.x >= 0) and (move.x < self.board.width))
                                        and ((move.y >= 0) and (move.y < self.board.height))))
        return (((move.x >= 0) and (move.x < self.board.width))
                and ((move.y >= 0) and (move.y < self.board.height)))

    def isValidMove(self, move):
        if (self.turn == 1):
            return self.isMoveOnBoard(move)
        else:
            return (self.isMoveOnBoard(move) and self.isMoveUnique(move))

    def makeMove(self, move):
        if self.isValidMove(move):
            self.board.placeToken(move)
            self.turn += 1
            return True
        else:
            return False

    def checkForWin(self):

        board = self.board.getBoard()

        for x in range(self.board.width):
            for y in range(self.board.height):
                if board[x][y] != None:

                    # Are these boundaries computed right??
                    x_fits_on_board = ( x + self.length_to_win < self.board.width )
                    y_fits_on_board = ( y + self.length_to_win < self.board.height )
                    diagf_fits_on_board = ( x + self.length_to_win < self.board.width ) and ( y + self.length_to_win < self.board.height )
                    diagb_fits_on_board = ( x + self.length_to_win < self.board.width ) and ( y - self.length_to_win > 0 )

                    # Generate lists of pieces on board
                    if x_fits_on_board:
                        x_set = list(set([board[x + delta][y] for delta in range(self.length_to_win)]))
                    else:
                        x_set = [None]

                    if y_fits_on_board:
                        y_set = list(set([board[x][y + delta] for delta in range(self.length_to_win)]))
                    else:
                        y_set = [None]

                    if diagf_fits_on_board:
                        diagf_set = list(set([board[x + delta][y + delta] for delta in range(self.length_to_win)]))
                    else:
                        diagf_set = [None]

                    if diagb_fits_on_board:
                        diagb_set = list(set([board[x + delta][y - delta] for delta in range(self.length_to_win)]))
                    else:
                        diagb_set = [None]

                    # Now check the responses

                    if ((len(x_set) == 1)):
                        color = x_set[0]
                        try:
                            below = board[x - 1][y]
                        except IndexError:
                            below = None
                        try:
                            above = board[x + self.length_to_win][y]
                        except IndexError:
                            above = None
                        if (color != below) and (color != above):
                            return True

                    if ((len(y_set) == 1)):
                        color = y_set[0]
                        try:
                            below = board[x][y - 1]
                        except IndexError:
                            below = None
                        try:
                            above = board[x][y + self.length_to_win]
                        except IndexError:
                            above = None
                        if (color != below) and (color != above):
                            return True

                    if ((len(diagf_set) == 1)):
                        color = diagf_set[0]
                        try:
                            below = board[x - 1][y - 1]
                        except IndexError:
                            below = None
                        try:
                            above = board[x + self.length_to_win][y + self.length_to_win]
                        except IndexError:
                            above = None
                        if (color != below) and (color != above):
                            return True

                    if ((len(diagb_set) == 1)):
                        color = diagb_set[0]
                        try:
                            below = board[x - 1][y - 1]
                        except IndexError:
                            below = None
                        try:
                            above = board[x + self.length_to_win][y + self.length_to_win]
                        except IndexError:
                            above = None
                        if (color != below) and (color != above):
                            return True
        return False

    def isBoardFull(self):
        return self.board.isFull()


def readMoveFile(move_file="move_file", purge=True):
    with open(move_file) as move_fid:
        line = move_fid.readline()
    if line is None:
        logging.error("Move file empty!")

    logging.debug("Read from %s: \"%s\"" % (move_file, line))

    line_parts = line.split()
    team_name = line_parts[0]
    #move_x = int(line_parts[1], 10)
    move_x = ord(line_parts[1].lower()) - ord('a')
    move_y = int(line_parts[2], 10)

    move = Move(team_name, move_x, move_y)

    if purge:
        os.remove(move_file)

    return move

def initMoveFile(move_file="move_file"):
    with open(move_file, 'w') as move_fid:
        pass
    return os.stat(move_file_name).st_mtime


def writeMoveFile(move, move_msg, move_file="move_file"):
    with open(move_file, 'w') as move_fid:
        move_text = str(move) # + " : " + move_msg
        logging.debug("Writing move text \"%s\" to %s" % (move_text, move_file))
        move_fid.write(move_text)
        move_fid.write("\n")
        move_fid.flush()
    return os.stat(move_file_name).st_mtime

def writeEndFile(move_msg, end_file="end_game"):
    with open(end_file, 'w') as end_fid:
        end_fid.write(move_msg)
        end_fid.write("\n")
        end_fid.flush()
    return os.stat(move_file_name).st_mtime


def getTeamFileName(team_name):
    return team_name + ".go"


def writeTeamGoFile(team_name):
    team_go_file = getTeamFileName(team_name)
    # time.sleep(0.7) # <------------------------------------------    NEW
    with open(team_go_file, 'w') as team_fid:
        team_fid.write("go!\n")
        team_fid.flush()
    return True


def removeTeamGoFile(team_name):
    team_go_file = getTeamFileName(team_name)
    if os.path.isfile(team_go_file):
        os.remove(team_go_file)


def waitForPlay(prev_mod_info, move_file_name="move_file"):

    played_in_time = True
    timeout = time.time() + turn_length_in_seconds
    while os.stat(move_file_name).st_mtime == prev_mod_info:
        if time.time() >= timeout:
            played_in_time = False
            break
        time.sleep(0.05)
    time.sleep(0.10) # a little bit extra to allow for file writeout to occur)
    return played_in_time


def play_gomoku(team1, team2):
    logging.info("Let the battle between %s and %s begin!" % (team1, team2))
    teams = [team1, team2]
    random.shuffle(teams)

    def opponentOf(team):
        return teams[ (teams.index(team) - 1) % len(teams) ]

    game = Game(board_width, board_height, length_to_win)

    playing_game = True
    move_file_mod_info = initMoveFile(move_file_name)
    while (playing_game):
        up_to_play = teams[ (game.turn % len(teams)) ]
        logging.info("%s's turn!" % up_to_play)

        writeTeamGoFile(up_to_play)
        played_in_time = waitForPlay(move_file_mod_info, move_file_name)
        move_msg = ""
        logging.debug("Played in time: %s" % played_in_time)
        if not played_in_time:
            logging.error("Out of time!")
            win_team = opponentOf(up_to_play)
            lose_team = up_to_play
            logging.info("%s loses!" % (win_team,))
            logging.info("%s wins!" % (lose_team,))
            move_msg = "END: %s WINS!  %s LOSES!  out of time" % (win_team, lose_team,)
            playing_game = False
            move = Move(up_to_play, -1, -1)
        else:

            move = readMoveFile(move_file_name, True)

            removeTeamGoFile(up_to_play)

            if move.team_name != up_to_play:
                logging.error("Wait your turn!")
                win_team = opponentOf(up_to_play)
                lose_team = up_to_play
                logging.info("%s loses!" % (win_team,))
                logging.info("%s wins!" % (lose_team,))
                move_msg = "END: %s WINS!  %s LOSES!  out of order move" % (win_team, lose_team,)
                playing_game = False
            elif not game.isValidMove(move):
                logging.error("Invalid move!")
                win_team = opponentOf(up_to_play)
                lose_team = up_to_play
                logging.info("%s loses!" % (win_team,))
                logging.info("%s wins!" % (lose_team,))
                move_msg = "END: %s WINS!  %s LOSES!  invalid move" % (win_team, lose_team,)
                playing_game = False
            else:
                game.makeMove(move)
                if game.checkForWin():
                    #logging.info("%s wins!" % (up_to_play))
                    #logging.info("%s loses!" % teams[ (game.turn + (teams.index(up_to_play)-1)) % len(teams) ])
                    #move_msg = "WIN : %s in a row!" % (game.length_to_win)
                    win_team = up_to_play
                    lose_team = opponentOf(up_to_play)
                    logging.info("%s loses!" % (win_team,))
                    logging.info("%s wins!" % (lose_team,))
                    move_msg = "END: %s WINS!  %s LOSES!  %s in a row!" % (win_team, lose_team, game.length_to_win)
                    playing_game = False

        if game.isBoardFull():
            logging.info("Tie game!")
            move_msg = "END: TIE! board full!"
            playing_game = False

        if playing_game:
            move_msg = "" #"KEEP GOING!"

        move_file_mod_info = writeMoveFile(move, move_msg, move_file_name)


        #playing_game = False
        logging.info("")
    writeEndFile(move_msg)
    for team in teams:
        writeTeamGoFile(team)





if __name__ == "__main__":
    # global board_width, board_height, length_to_win
    if len(sys.argv) == 3:
        team1 = sys.argv[1]
        team2 = sys.argv[2]
    elif len(sys.argv) == 6:
        team1 = sys.argv[1]
        team2 = sys.argv[2]
        board_width = int(sys.argv[3])
        board_height = int(sys.argv[4])
        length_to_win = int(sys.argv[5])
    else:
        logging.error("Wrong number of arguments!  Call is %s [team1_name] [team2_name]")
        exit(8)
    play_gomoku(team1, team2)