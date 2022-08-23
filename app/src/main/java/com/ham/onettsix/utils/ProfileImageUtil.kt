package com.ham.onettsix.utils

import com.ham.onettsix.R

class ProfileImageUtil {

    /* 오소리, 곰, 물소, 황소, 낙타, 고양이, 치타, 젖소, 악어, 사슴, 개,
    코끼리, 가젤, 기린, 염소, 말, 캥거루, 코알라, 사자, 원숭이, 펭귄, 돼지,
    북극곰, 토끼, 순록, 코뿔소, 상어, 양, 거북이, 늑대, 얼룩말
    * */
    companion object {
        fun getImageId(profileImageId: Int): Int {
            var id = 0
            when (profileImageId) {
                0 -> {
                    id = R.drawable.ic_profile_badger
                }
                1 -> {
                    id = R.drawable.ic_profile_bear
                }
                2 -> {
                    id = R.drawable.ic_profile_buffalo
                }
                3 -> {
                    id = R.drawable.ic_profile_bull
                }
                4 -> {
                    id = R.drawable.ic_profile_camel
                }
                5 -> {
                    id = R.drawable.ic_profile_cat
                }
                6 -> {
                    id = R.drawable.ic_profile_cheetah
                }
                7 -> {
                    id = R.drawable.ic_profile_cow
                }
                8 -> {
                    id = R.drawable.ic_profile_crocodile
                }
                9 -> {
                    id = R.drawable.ic_profile_deer
                }
                10 -> {
                    id = R.drawable.ic_profile_dog
                }
                11 -> {
                    id = R.drawable.ic_profile_elephant
                }
                12 -> {
                    id = R.drawable.ic_profile_gazelle
                }
                13 -> {
                    id = R.drawable.ic_profile_giraffe
                }
                14 -> {
                    id = R.drawable.ic_profile_horse
                }
                15 -> {
                    id = R.drawable.ic_profile_kangaroo
                }
                16 -> {
                    id = R.drawable.ic_profile_koala
                }
                17 -> {
                    id = R.drawable.ic_profile_lion
                }
                18 -> {
                    id = R.drawable.ic_profile_monkey
                }
                19 -> {
                    id = R.drawable.ic_profile_penguin
                }
                20 -> {
                    id = R.drawable.ic_profile_pig
                }
                21 -> {
                    id = R.drawable.ic_profile_polar_bear
                }
                22 -> {
                    id = R.drawable.ic_profile_rabbit
                }
                23 -> {
                    id = R.drawable.ic_profile_reindeer
                }
                24 -> {
                    id = R.drawable.ic_profile_rhino
                }
                25 -> {
                    id = R.drawable.ic_profile_shark
                }
                26 -> {
                    id = R.drawable.ic_profile_sheep
                }
                27 -> {
                    id = R.drawable.ic_profile_turtle
                }
                28 -> {
                    id = R.drawable.ic_profile_wolf
                }
                29 -> {
                    id = R.drawable.ic_profile_zebra
                }
            }
            return id
        }
    }
}