package com.learnwithsubs.feature_video_list.usecase

import android.content.Context
import android.widget.Toast
import com.learnwithsubs.feature_video_list.models.Video
import com.learnwithsubs.feature_video_list.repository.ServerInteractionRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class SendAudioToServerUseCase(
    private val serverInteractionRepository: ServerInteractionRepository
) {
    suspend fun invoke(video: Video?) {
        if (video != null) { // TODO
            val file =  File("${video.outputPath}.mp3")
            val requestFile = file.asRequestBody("audio/mp3".toMediaType())
            val audioPart = MultipartBody.Part.createFormData("audio", file.name, requestFile)

            try {
                val response = serverInteractionRepository.sendAudioToServer(audioPart)
                if (response.isSuccessful) {
                    handleSubtitlesResponse(video, response.body())
                } else { // TODO
                    handleSubtitlesResponse(video, """1
00:00:00,000 --> 00:00:03,000
Good and beaters my lesser the power of media

2
00:00:03,000 --> 00:00:06,000
Secret I don't wanna know what's all mysterious

3
00:00:06,000 --> 00:00:09,000
Even though you suicide but I for control there yeah

4
00:00:09,000 --> 00:00:11,000
Confident perfect all you say is a angel

5
00:00:11,000 --> 00:00:14,000
Steer misgenius I don't amass

6
00:00:14,000 --> 00:00:20,000
What's it to you today? What back do you love?

7
00:00:20,000 --> 00:00:23,000
Whenever you go up open tell me where do you go?

8
00:00:23,000 --> 00:00:26,000
Haven't eaten anything? It's a secret unknown

9
00:00:26,000 --> 00:00:29,000
Any question you're busy always acting so vaguely

10
00:00:29,000 --> 00:00:31,000
I'm concerned although you're brightly slow

11
00:00:31,000 --> 00:00:34,000
And seemingly unveiled secrets or as speed as honey

12
00:00:34,000 --> 00:00:36,000
Confusing white white white

13
00:00:36,000 --> 00:00:37,000
Extinguish all my light light

14
00:00:37,000 --> 00:00:39,000
So what is your type of guy? Any fight or soul?

15
00:00:39,000 --> 00:00:40,000
Now I said it

16
00:00:40,000 --> 00:00:44,000
I don't have any idea how I could love anyone

17
00:00:44,000 --> 00:00:46,000
I don't seem to know what is signified

18
00:00:46,000 --> 00:00:49,000
Can I find better fits through or at the light?

19
00:00:49,000 --> 00:00:51,000
Once I get there some badby

20
00:00:51,000 --> 00:00:53,000
First fall and fall and walls and cues

21
00:00:53,000 --> 00:00:55,000
Made in loses and ever you

22
00:00:55,000 --> 00:00:57,000
That emotion melt to art to lie to you

23
00:00:57,000 --> 00:01:00,000
Cause you are perfect and lost to ten

24
00:01:00,000 --> 00:01:02,000
I don't own a rainbow

25
00:01:02,000 --> 00:01:03,000
We're not a fear again

26
00:01:03,000 --> 00:01:05,000
Is the brightest star

27
00:01:05,000 --> 00:01:07,000
Reborn yes in the air

28
00:01:07,000 --> 00:01:08,000
You see the smell of face

29
00:01:08,000 --> 00:01:10,000
Now I love you again

30
00:01:10,000 --> 00:01:12,000
Now everybody is worth it

31
00:01:12,000 --> 00:01:13,000
Got to fight it by you

32
00:01:13,000 --> 00:01:14,000
The beautiful that you got

33
00:01:14,000 --> 00:01:16,000
Do you book out of every way?

34
00:01:16,000 --> 00:01:18,000
Night you're as your first perfect eye

35
00:01:18,000 --> 00:01:21,000
Right right we are not just very special yes

36
00:01:21,000 --> 00:01:24,000
We had lost our pipe before I saw this song and

37
00:01:24,000 --> 00:01:27,000
Make some such a star with serving as a part of her grace

38
00:01:27,000 --> 00:01:30,000
Can I tell me everything was cause of her? No, it's not bright

39
00:01:30,000 --> 00:01:32,000
I don't try and how can we not do it

40
00:01:32,000 --> 00:01:33,000
You're as wild being around

41
00:01:33,000 --> 00:01:34,000
It's not you can arise

42
00:01:34,000 --> 00:01:36,000
I cannot forgive you for that conflict

43
00:01:36,000 --> 00:01:37,000
Is it not everything that's right?

44
00:01:37,000 --> 00:01:38,000
We're not a problem alone

45
00:01:38,000 --> 00:01:40,000
I wouldn't let anyone fit

46
00:01:40,000 --> 00:01:41,000
Not just strong as a law

47
00:01:41,000 --> 00:01:43,000
Got emotions, these so hard

48
00:01:43,000 --> 00:01:44,000
We're shopping you

49
00:01:44,000 --> 00:01:45,000
Yes indeed it's so strong

50
00:01:45,000 --> 00:01:47,000
It's too reliable, I don't care

51
00:01:47,000 --> 00:01:49,000
There's enough you wait

52
00:01:49,000 --> 00:01:50,000
This is the fight

53
00:01:50,000 --> 00:01:52,000
The brightest star is residing

54
00:01:52,000 --> 00:01:54,000
And you pick out the shirt

55
00:01:54,000 --> 00:01:56,000
Got names to show and then it's gonna be

56
00:01:56,000 --> 00:01:57,000
Farts no bad

57
00:01:57,000 --> 00:01:59,000
You want to know she's dreaming

58
00:01:59,000 --> 00:02:01,000
Well, normally it's different

59
00:02:01,000 --> 00:02:03,000
No, I know it's such a truth

60
00:02:03,000 --> 00:02:05,000
It's so real aside

61
00:02:05,000 --> 00:02:07,000
Showing this my my weapon

62
00:02:07,000 --> 00:02:08,000
Boyle media

63
00:02:08,000 --> 00:02:11,000
Kidding everything about my secret deep inside

64
00:02:11,000 --> 00:02:12,000
I'll be love with you

65
00:02:12,000 --> 00:02:14,000
Make careerless built on such a light

66
00:02:14,000 --> 00:02:17,000
It's the way I know to show my love without a doubt

67
00:02:17,000 --> 00:02:21,000
Running down my sweat as flowing clean as aqua bright

68
00:02:21,000 --> 00:02:23,000
Roozy head and under my head

69
00:02:23,000 --> 00:02:24,000
Let's wear echoey sides

70
00:02:24,000 --> 00:02:26,000
A same gun that's so round

71
00:02:26,000 --> 00:02:27,000
Look at me, I'm Maria

72
00:02:27,000 --> 00:02:29,000
Soul light, surely

73
00:02:29,000 --> 00:02:31,000
It's the greatest kind of love

74
00:02:31,000 --> 00:02:34,000
I recall no one thought love

75
00:02:34,000 --> 00:02:35,000
Me whole before

76
00:02:35,000 --> 00:02:36,000
And if not, then I love

77
00:02:36,000 --> 00:02:38,000
With anybody before

78
00:02:38,000 --> 00:02:40,000
Now the lies I'm making up

79
00:02:40,000 --> 00:02:42,000
I'm hoping that I take home

80
00:02:42,000 --> 00:02:44,000
When they all become true and I

81
00:02:44,000 --> 00:02:45,000
Kid, what should I do?

82
00:02:45,000 --> 00:02:48,000
One day I will hold everything that I pursue

83
00:02:48,000 --> 00:02:50,000
Yes I am so free to do

84
00:02:50,000 --> 00:02:51,000
For gracious I know

85
00:02:51,000 --> 00:02:53,000
So stay here, we want

86
00:02:53,000 --> 00:02:55,000
I wish your force to love each other

87
00:02:55,000 --> 00:02:57,000
With love my heart and soul

88
00:02:57,000 --> 00:02:59,000
So that I lie again

89
00:02:59,000 --> 00:03:00,000
The words I vocalize

90
00:03:00,000 --> 00:03:02,000
And settle me and wish it

91
00:03:02,000 --> 00:03:03,000
The one day we got true

92
00:03:03,000 --> 00:03:04,000
I feel they say I'm not

93
00:03:04,000 --> 00:03:06,000
But I'm able to let you in

94
00:03:06,000 --> 00:03:07,000
You hear me saying

95
00:03:07,000 --> 00:03:08,000
You're leaving me for worse

96
00:03:08,000 --> 00:03:10,000
I said it at last

97
00:03:10,000 --> 00:03:12,000
And now it's not a lie

98
00:03:12,000 --> 00:03:14,000
As I'm losing these words, I love you

99
00:03:20,000 --> 00:03:28,000
For I said it at last

100
00:03:28,000 --> 00:03:30,000
And now it's not a lie""".trimIndent())
                }
            } catch (e: Exception) { // TODO
                handleSubtitlesResponse(video, """1
00:00:00,000 --> 00:00:03,000
Good and beaters my lesser the power of media

2
00:00:03,000 --> 00:00:06,000
Secret I don't wanna know what's all mysterious

3
00:00:06,000 --> 00:00:09,000
Even though you suicide but I for control there yeah

4
00:00:09,000 --> 00:00:11,000
Confident perfect all you say is a angel

5
00:00:11,000 --> 00:00:14,000
Steer misgenius I don't amass

6
00:00:14,000 --> 00:00:20,000
What's it to you today? What back do you love?

7
00:00:20,000 --> 00:00:23,000
Whenever you go up open tell me where do you go?

8
00:00:23,000 --> 00:00:26,000
Haven't eaten anything? It's a secret unknown

9
00:00:26,000 --> 00:00:29,000
Any question you're busy always acting so vaguely

10
00:00:29,000 --> 00:00:31,000
I'm concerned although you're brightly slow

11
00:00:31,000 --> 00:00:34,000
And seemingly unveiled secrets or as speed as honey

12
00:00:34,000 --> 00:00:36,000
Confusing white white white

13
00:00:36,000 --> 00:00:37,000
Extinguish all my light light

14
00:00:37,000 --> 00:00:39,000
So what is your type of guy? Any fight or soul?

15
00:00:39,000 --> 00:00:40,000
Now I said it

16
00:00:40,000 --> 00:00:44,000
I don't have any idea how I could love anyone

17
00:00:44,000 --> 00:00:46,000
I don't seem to know what is signified

18
00:00:46,000 --> 00:00:49,000
Can I find better fits through or at the light?

19
00:00:49,000 --> 00:00:51,000
Once I get there some badby

20
00:00:51,000 --> 00:00:53,000
First fall and fall and walls and cues

21
00:00:53,000 --> 00:00:55,000
Made in loses and ever you

22
00:00:55,000 --> 00:00:57,000
That emotion melt to art to lie to you

23
00:00:57,000 --> 00:01:00,000
Cause you are perfect and lost to ten

24
00:01:00,000 --> 00:01:02,000
I don't own a rainbow

25
00:01:02,000 --> 00:01:03,000
We're not a fear again

26
00:01:03,000 --> 00:01:05,000
Is the brightest star

27
00:01:05,000 --> 00:01:07,000
Reborn yes in the air

28
00:01:07,000 --> 00:01:08,000
You see the smell of face

29
00:01:08,000 --> 00:01:10,000
Now I love you again

30
00:01:10,000 --> 00:01:12,000
Now everybody is worth it

31
00:01:12,000 --> 00:01:13,000
Got to fight it by you

32
00:01:13,000 --> 00:01:14,000
The beautiful that you got

33
00:01:14,000 --> 00:01:16,000
Do you book out of every way?

34
00:01:16,000 --> 00:01:18,000
Night you're as your first perfect eye

35
00:01:18,000 --> 00:01:21,000
Right right we are not just very special yes

36
00:01:21,000 --> 00:01:24,000
We had lost our pipe before I saw this song and

37
00:01:24,000 --> 00:01:27,000
Make some such a star with serving as a part of her grace

38
00:01:27,000 --> 00:01:30,000
Can I tell me everything was cause of her? No, it's not bright

39
00:01:30,000 --> 00:01:32,000
I don't try and how can we not do it

40
00:01:32,000 --> 00:01:33,000
You're as wild being around

41
00:01:33,000 --> 00:01:34,000
It's not you can arise

42
00:01:34,000 --> 00:01:36,000
I cannot forgive you for that conflict

43
00:01:36,000 --> 00:01:37,000
Is it not everything that's right?

44
00:01:37,000 --> 00:01:38,000
We're not a problem alone

45
00:01:38,000 --> 00:01:40,000
I wouldn't let anyone fit

46
00:01:40,000 --> 00:01:41,000
Not just strong as a law

47
00:01:41,000 --> 00:01:43,000
Got emotions, these so hard

48
00:01:43,000 --> 00:01:44,000
We're shopping you

49
00:01:44,000 --> 00:01:45,000
Yes indeed it's so strong

50
00:01:45,000 --> 00:01:47,000
It's too reliable, I don't care

51
00:01:47,000 --> 00:01:49,000
There's enough you wait

52
00:01:49,000 --> 00:01:50,000
This is the fight

53
00:01:50,000 --> 00:01:52,000
The brightest star is residing

54
00:01:52,000 --> 00:01:54,000
And you pick out the shirt

55
00:01:54,000 --> 00:01:56,000
Got names to show and then it's gonna be

56
00:01:56,000 --> 00:01:57,000
Farts no bad

57
00:01:57,000 --> 00:01:59,000
You want to know she's dreaming

58
00:01:59,000 --> 00:02:01,000
Well, normally it's different

59
00:02:01,000 --> 00:02:03,000
No, I know it's such a truth

60
00:02:03,000 --> 00:02:05,000
It's so real aside

61
00:02:05,000 --> 00:02:07,000
Showing this my my weapon

62
00:02:07,000 --> 00:02:08,000
Boyle media

63
00:02:08,000 --> 00:02:11,000
Kidding everything about my secret deep inside

64
00:02:11,000 --> 00:02:12,000
I'll be love with you

65
00:02:12,000 --> 00:02:14,000
Make careerless built on such a light

66
00:02:14,000 --> 00:02:17,000
It's the way I know to show my love without a doubt

67
00:02:17,000 --> 00:02:21,000
Running down my sweat as flowing clean as aqua bright

68
00:02:21,000 --> 00:02:23,000
Roozy head and under my head

69
00:02:23,000 --> 00:02:24,000
Let's wear echoey sides

70
00:02:24,000 --> 00:02:26,000
A same gun that's so round

71
00:02:26,000 --> 00:02:27,000
Look at me, I'm Maria

72
00:02:27,000 --> 00:02:29,000
Soul light, surely

73
00:02:29,000 --> 00:02:31,000
It's the greatest kind of love

74
00:02:31,000 --> 00:02:34,000
I recall no one thought love

75
00:02:34,000 --> 00:02:35,000
Me whole before

76
00:02:35,000 --> 00:02:36,000
And if not, then I love

77
00:02:36,000 --> 00:02:38,000
With anybody before

78
00:02:38,000 --> 00:02:40,000
Now the lies I'm making up

79
00:02:40,000 --> 00:02:42,000
I'm hoping that I take home

80
00:02:42,000 --> 00:02:44,000
When they all become true and I

81
00:02:44,000 --> 00:02:45,000
Kid, what should I do?

82
00:02:45,000 --> 00:02:48,000
One day I will hold everything that I pursue

83
00:02:48,000 --> 00:02:50,000
Yes I am so free to do

84
00:02:50,000 --> 00:02:51,000
For gracious I know

85
00:02:51,000 --> 00:02:53,000
So stay here, we want

86
00:02:53,000 --> 00:02:55,000
I wish your force to love each other

87
00:02:55,000 --> 00:02:57,000
With love my heart and soul

88
00:02:57,000 --> 00:02:59,000
So that I lie again

89
00:02:59,000 --> 00:03:00,000
The words I vocalize

90
00:03:00,000 --> 00:03:02,000
And settle me and wish it

91
00:03:02,000 --> 00:03:03,000
The one day we got true

92
00:03:03,000 --> 00:03:04,000
I feel they say I'm not

93
00:03:04,000 --> 00:03:06,000
But I'm able to let you in

94
00:03:06,000 --> 00:03:07,000
You hear me saying

95
00:03:07,000 --> 00:03:08,000
You're leaving me for worse

96
00:03:08,000 --> 00:03:10,000
I said it at last

97
00:03:10,000 --> 00:03:12,000
And now it's not a lie

98
00:03:12,000 --> 00:03:14,000
As I'm losing these words, I love you

99
00:03:20,000 --> 00:03:28,000
For I said it at last

100
00:03:28,000 --> 00:03:30,000
And now it's not a lie""".trimIndent())
            }
        }
    }

    private fun handleSubtitlesResponse(video: Video, response: String?) {
        if (response == null) { // TODO
        }
        try {
            val subSTR = File("${video.outputPath}.srt")
            if (subSTR.exists())
                subSTR.delete()
            subSTR.createNewFile()
            val writer = subSTR.bufferedWriter()
            writer.write(response)
            writer.close()
        }catch (e: Exception) {
            // TODO edit toast
            //Toast.makeText(context.applicationContext, "Write error = $e.", Toast.LENGTH_SHORT).show()
        }
    }
}