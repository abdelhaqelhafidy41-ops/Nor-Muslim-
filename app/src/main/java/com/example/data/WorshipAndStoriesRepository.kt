package com.example.data

import com.example.model.IslamicStory
import com.example.model.PlanTask
import com.example.model.StoryCategory
import com.example.model.WorshipPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorshipAndStoriesRepository {

    private val _plans = MutableStateFlow(
        listOf(
            WorshipPlan(
                id = "plan_daily_muslim",
                title = "خطة المسلم اليومية المتوازنة",
                description = "برنامج يومي متكامل يجمع بين الفرائض والسنن الرواتب وأذكار الصباح والمساء والورد القرآني",
                category = "الخطط اليومية",
                durationDays = 30,
                currentDay = 12,
                tasks = listOf(
                    PlanTask("t1", "صلاة الفجر في وقتها مع أذكار الصباح", true),
                    PlanTask("t2", "قراءة جزء كامل من القرآن الكريم", true),
                    PlanTask("t3", "المحافظة على السنن الرواتب (١٢ ركعة)", true),
                    PlanTask("t4", "أذكار المساء وصلاة المغرب بالمسجد", false),
                    PlanTask("t5", "صلاة الشفع والوتر قبل النوم", false),
                    PlanTask("t6", "١٠٠ تسبيحة و١٠٠ استغفار خلال اليوم", true)
                )
            ),
            WorshipPlan(
                id = "plan_khatma_month",
                title = "خطة ختم القرآن الكريم في شهر",
                description = "قراءة جزء واحد يومياً بعد كل صلاة ٤ صفحات لإنهاء المصحف في ٣٠ يوماً بتدبر وسكينة",
                category = "ختم القرآن",
                durationDays = 30,
                currentDay = 18,
                tasks = listOf(
                    PlanTask("tk1", "٤ صفحات عقب صلاة الفجر", true),
                    PlanTask("tk2", "٤ صفحات عقب صلاة الظهر", true),
                    PlanTask("tk3", "٤ صفحات عقب صلاة العصر", true),
                    PlanTask("tk4", "٤ صفحات عقب صلاة المغرب", false),
                    PlanTask("tk5", "٤ صفحات عقب صلاة العشاء", false)
                )
            ),
            WorshipPlan(
                id = "plan_ramadan",
                title = "خطة رمضان المبارك (سباق الخيرات)",
                description = "برنامج إيماني مكثف لشهر الصيام والقرآن وتكفير السيئات والاعتكاف",
                category = "رمضان",
                durationDays = 30,
                currentDay = 1,
                tasks = listOf(
                    PlanTask("tr1", "السحور واستغفار الأسحار", true),
                    PlanTask("tr2", "صلاة التراويح وقيام الليل كاملة", true),
                    PlanTask("tr3", "ختمتان للقرآن (جزءان يومياً)", false),
                    PlanTask("tr4", "إفطار صائم أو صدقة يومية", true),
                    PlanTask("tr5", "دعاء الإفطار وتفقد الأرحام والمحتاجين", true)
                )
            ),
            WorshipPlan(
                id = "plan_qiyam",
                title = "خطة الاستقامة على قيام الليل",
                description = "التدرج في ركعات الليل بدءاً من ركعتين خفيفتين قبيل الفجر مع التضرع والاستغفار",
                category = "قيام الليل",
                durationDays = 21,
                currentDay = 7,
                tasks = listOf(
                    PlanTask("tq1", "النوم على طهارة مبكراً", true),
                    PlanTask("tq2", "الاستيقاظ قبل الفجر بنصف ساعة", true),
                    PlanTask("tq3", "صلاة ركعتين طويلتين بسكينة وتدبر", true),
                    PlanTask("tq4", "الاستغفار بالأسحار والدعاء للنفس والأمة", false)
                )
            ),
            WorshipPlan(
                id = "plan_friday",
                title = "خطة يوم الجمعة المبارك",
                description = "سنن الجمعة الجامعة: قراءة سورة الكهف، الغسل، التبكير، والإكثار من الصلاة على النبي ﷺ",
                category = "الأيام الفاضلة",
                durationDays = 1,
                currentDay = 1,
                tasks = listOf(
                    PlanTask("tf1", "الغسل والتطيب ولبس أحسن الثياب", true),
                    PlanTask("tf2", "التبكير إلى المسجد وصلاة تحية المسجد", true),
                    PlanTask("tf3", "قراءة سورة الكهف كاملة بتدبر", true),
                    PlanTask("tf4", "١٠٠٠ صلاة على الحبيب المصطفى ﷺ", true),
                    PlanTask("tf5", "تحري ساعة الاستجابة في آخر ساعة من العصر", false)
                )
            ),
            WorshipPlan(
                id = "plan_fasting_sunnah",
                title = "خطة صيام الإثنين والخميس والأيام البيض",
                description = "إحياء سنة الصيام الأسبوعي وعرض الأعمال على الله والعبد صائم",
                category = "الصيام",
                durationDays = 30,
                currentDay = 9,
                tasks = listOf(
                    PlanTask("tfs1", "تبييت نية الصيام من الليل", true),
                    PlanTask("tfs2", "سحور خفيف مع التمر والماء", true),
                    PlanTask("tfs3", "حفظ اللسان والجوارح عن اللغو", true),
                    PlanTask("tfs4", "دعوة مستجابة عند الإفطار", true)
                )
            ),
            WorshipPlan(
                id = "plan_dhulhijjah",
                title = "خطة العشر الأوائل من ذي الحجة ويوم عرفة",
                description = "أفضل أيام الدنيا: التكبير والتهليل والتحميد والصدقة وصيام يوم عرفة لغير الحاج",
                category = "المواسم الفاضلة",
                durationDays = 10,
                currentDay = 3,
                tasks = listOf(
                    PlanTask("td1", "إحياء التكبير المطلق والمقيد", true),
                    PlanTask("td2", "صيام التسع الأوائل وخاصة يوم عرفة", true),
                    PlanTask("td3", "إخراج صدقة يومية للمحتاجين", true),
                    PlanTask("td4", "الإكثار من: لا إله إلا الله وحده لا شريك له", false)
                )
            ),
            WorshipPlan(
                id = "plan_beginners",
                title = "خطة المبتدئين في العبادة",
                description = "منهج مبسط خطوة بخطوة للثبات على الصلوات الخمس وإتقان الوضوء والأذكار الأساسية",
                category = "المبتدئين",
                durationDays = 14,
                currentDay = 4,
                tasks = listOf(
                    PlanTask("tb1", "أداء الصلوات الخمس في أوقاتها دون تأخير", true),
                    PlanTask("tb2", "قراءة صفحتين من القرآن يومياً", true),
                    PlanTask("tb3", "ترديد أذكار الصباح والمساء الأساسية", false)
                )
            )
        )
    )
    val plans: StateFlow<List<WorshipPlan>> = _plans.asStateFlow()

    fun toggleTask(planId: String, taskId: String) {
        val updated = _plans.value.map { plan ->
            if (plan.id == planId) {
                val newTasks = plan.tasks.map { task ->
                    if (task.id == taskId) task.copy(isDone = !task.isDone) else task
                }
                plan.copy(tasks = newTasks)
            } else plan
        }
        _plans.value = updated
    }

    fun addCustomPlan(plan: WorshipPlan) {
        _plans.value = listOf(plan) + _plans.value
    }

    val stories = listOf(
        IslamicStory(
            id = "seerah_hijrah",
            category = StoryCategory.SEERAH,
            title = "الهجرة النبوية الشريفة وتأسيس مجتمع المدينة",
            summary = "رحلة الإيمان والتضحية الكبرى من مكة المكرمة إلى يثرب، وصحبة أبي بكر الصديق رضي الله عنه في الغار.",
            content = "لما اشتد أذى قريش على النبي ﷺ وأصحابه، أذن الله لنبيه بالهجرة. خرج النبي ﷺ مع صاحبه أبي بكر الصديق سراً متوجهين إلى غار ثور، وهناك تجلت معية الله العظيمة حين قال النبي ﷺ لصاحبه: «ما ظنك يا أبا بكر باثنين الله ثالثهما؟». ثم وصلا إلى المدينة المنورة فاستقبلهما الأنصار بأهازيج الفرح، وآخى النبي بين المهاجرين والأنصار وأرسى وثيقة المدينة التي أسست أول دولة للعدل والحرية في التاريخ.",
            lessons = listOf(
                "الأخذ بالأسباب كاملة مع اليقين المطلق بأن التوفيق والنصر بيد الله وحده",
                "أهمية الصاحب الصالح في طريق الإيمان وثبات أبي بكر الصديق رضي الله عنه",
                "المؤاخاة بين المهاجرين والأنصار أعظم نموذج للإيثار والتكافل الإنساني"
            ),
            authenticSource = "السيرة النبوية لابن هشام / الرحيق المختوم"
        ),
        IslamicStory(
            id = "sahabah_abubakr",
            category = StoryCategory.COMPANIONS,
            title = "أبو بكر الصديق رضي الله عنه (أول الخلفاء الراشدين)",
            summary = "صاحب رسول الله ﷺ وثاني اثنين في الغار، وأحب الرجال إلى قلبه، وخليفته الراشد الأول.",
            content = "هو عبد الله بن أبي قحافة، لُقّب بالصديق لتصديقه النبي ﷺ في خبر الإسراء والمعراج دون تردد. أنفق ماله كله في سبيل الله، وأعتق المستضعفين كبلال بن رباح. قال عنه النبي ﷺ: «لو كنت متخذاً خليلاً غير ربي لاتخذت أبا بكر خليلاً». وقف كالجبل الأشم يوم وفاة النبي ﷺ وجمع الأمة، وثبت في حروب الردة وجمع القرآن الكريم في مصحف واحد.",
            lessons = listOf(
                "اليقين الراسخ والتصديق الذي لا تزعزعه الشبهات",
                "البذل التام للنفس والمال في سبيل نصرة الحق",
                "الحكمة والرباطة في الأزمات الشديدة وجمع كلمة المسلمين"
            ),
            authenticSource = "صحيح البخاري ومسلم / سير أعلام النبلاء للذهبي"
        ),
        IslamicStory(
            id = "sahabah_umar",
            category = StoryCategory.COMPANIONS,
            title = "عمر بن الخطاب الفاروق رضي الله عنه",
            summary = "الذي أعز الله به الإسلام، ففرق به بين الحق والباطل، وضرب أروع الأمثلة في العدل والزهد ورعاية الرعية.",
            content = "أسلم بدعوة النبي ﷺ: «اللهم أعز الإسلام بأحب هذين الرجلين إليك». كان إسلامه فتحاً وهجرته نصراً وإمارته رحمة. في عهده فُتحت بلاد الشام ومصر وفارس، وكان يطوف بالليل يتفقد الأرامل واليتامى ويحمل الدقيق على ظهره، ويقول: «لو عثرت بغلة في العراق لسألني الله عنها لمَ لمْ تمهد لها الطريق يا عمر!».",
            lessons = listOf(
                "العدل المطلق أساس الحكم والأمان في المجتمعات",
                "الشعور العظيم بالمسؤولية أمام الله تجاه كل فرد",
                "الزهد في الدنيا مع امتلاك القوة والتمكين"
            ),
            authenticSource = "صحيح البخاري / تاريخ الطبري / مناقب عمر لابن الجوزي"
        ),
        IslamicStory(
            id = "sahabiyat_khadijah",
            category = StoryCategory.SAHABIYAT,
            title = "أم المؤمنين خديجة بنت خويلد رضي الله عنها",
            summary = "أول من آمن بالنبي ﷺ من الرجال والنساء، وسند النبوة الأعظم التي بشرها جبريل ببيت في الجنة من قصب.",
            content = "كانت سيدة نساء قريش عفةً وحكمةً. لما نزل الوحي على رسول الله ﷺ في غار حراء ورجع ترجف بوادره، ثبّتته بكلماتها الخالدة: «كلا والله ما يخزيك الله أبداً، إنك لتصل الرحم، وتحمل الكل، وتكسب المعدوم، وتقري الضيف، وتعين على نوائب الحق». أيدت الرسول بمالها ونفسها وحبها الصادق حتى لُقّب العام الذي توفيت فيه بعام الحزن.",
            lessons = listOf(
                "دور الزوجة الصالحة في التثبيت والطمأنينة وإزالة المخاوف",
                "حسن الخلق ومساعدة الضعفاء سبب للنجاة من نوائب الدهر",
                "الوفاء للزوج والمساندة في أوقات الشدة أثمن ما يقدمه الشريك"
            ),
            authenticSource = "صحيح البخاري (رقم ٣) / الإصابة في تمييز الصحابة"
        ),
        IslamicStory(
            id = "prophets_yusuf",
            category = StoryCategory.PROPHETS,
            title = "نبي الله يوسف عليه السلام (أحسن القصص)",
            summary = "من ظلمات الجب والاسترقاق وظلم السجن، إلى عزة التمكين وإدارة خزائن الأرض والصفح الجميل.",
            content = "قصة ضربت أروع الأمثلة في الصبر على كيد الإخوة وفتنة الشهوة وظلم السجن. استعصم يوسف بربه وقال: «مَعَاذَ اللَّهِ ۖ إِنَّهُ رَبِّي أَحْسَنَ مَثْوَايَ». وبعد سنوات المحنة آتاه الله الملك والحكمة، وحين جاءه إخوته مذنبين لم ينتقم بل قال بعفو الأنبياء العظيم: «لَا تَثْرِيبَ عَلَيْكُمُ الْيَوْمَ ۖ يَغْفِرُ اللَّهُ لَكُمْ ۖ وَهُوَ أَرْحَمُ الرَّاحِمِينَ».",
            lessons = listOf(
                "عاقبة الصبر والعفة هي التمكين والرفعة في الدنيا والآخرة",
                "حسن الظن بالله بأن بعد كل محنة منحة وفرجاً",
                "العفو عند المقدرة أعظم شيم الكرام والأنبياء"
            ),
            authenticSource = "القرآن الكريم (سورة يوسف) / تفسير ابن كثير"
        )
    )
}
