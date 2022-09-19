package FECharMaker

import java.awt.Color

object GameColors {
    private def hex(hexcode: String): Color =
        Color.decode("#"+hexcode)
        
    type ColorValPalette = Map[ColorIndexVal, Color]
    object ColorValPalette {
        def common( normal: String, brighter: String, darker: String ): ColorValPalette =
            common( hex(normal), hex(brighter), hex(darker) )
        def common( normal: Color, brighter: Color, darker: Color ): ColorValPalette =
            Map( ColorIndexVal.Normal -> normal, ColorIndexVal.Lighter(1) -> brighter, ColorIndexVal.Darker(1) -> darker )
    
        def hairhex( brighter: String, normal: String, darker: String ): ColorValPalette =
            common( normal, brighter, darker )

        def skin( brighter: String, normal: String, dark1: String, dark2: String, dark3: String ): ColorValPalette =
            skin( hex(brighter), hex(normal), hex(dark1), hex(dark2), hex(dark3) )
        def skin( brighter: Color, normal: Color, dark1: Color, dark2: Color, dark3: Color ): ColorValPalette =
            Map( ColorIndexVal.Normal -> normal, ColorIndexVal.Lighter(1) -> brighter,
                ColorIndexVal.Darker(1) -> dark1, ColorIndexVal.Darker(2) -> dark2, ColorIndexVal.Darker(3) -> dark3,
        )
        def to_shades( cvp: ColorValPalette, cidxs: ColorIndices ): Map[Int, Color] =
            cidxs.mapValues( cvp(_) ).toMap
        
        def normal_color( cvp: ColorValPalette ) =
            cvp( ColorIndexVal.Normal )
        
    }

    val DEFAULT_BORDER_COLOR = hex("382040")

    val DEFAULT_SKIN_COLOR = ColorValPalette.skin( "F8F8C0", "F8D070", "E89850", "987048", "604848" )

    val HairColors: List[Color] = List(  
        hex("e89088"), hex("d8847c"), hex("d8646c"), hex("d45870"), hex("bc3c58")
      , hex("f08878"), hex("f86860"), hex("f06060"), hex("e84c54"), hex("dc3848")
      , hex("ec3848"), hex("e02c44"), hex("d02838"), hex("c02034"), hex("b01830")
      , hex("f8404c"), hex("f03848"), hex("dc2c3c"), hex("d42434"), hex("c42030")
      , hex("f83830"), hex("f8302c"), hex("f42c24"), hex("f0241c"), hex("ec1c18")
      , hex("d04c38"), hex("dc3c34"), hex("e83c38"), hex("dc3034"), hex("e43c28")
      , hex("fc8864"), hex("f07048"), hex("ec6838"), hex("e84824"), hex("ec542c")
      , hex("f88c38"), hex("f48424"), hex("f88028"), hex("ec6424"), hex("e85c1c")
      , hex("fc9450"), hex("f8ac60"), hex("f0b058"), hex("e89c4c"), hex("d08838")
      , hex("d8a050"), hex("cc9448"), hex("c0803c"), hex("a86834"), hex("9c6030")
      , hex("f4e498"), hex("e8d888"), hex("dcc484"), hex("d0b470"), hex("c09c50")
      , hex("f4f8d0"), hex("f4f8bc"), hex("f0f8bc"), hex("e4dca8"), hex("e0c490")
      , hex("ecd088"), hex("e4d86c"), hex("e8d854"), hex("d8dc60"), hex("e0e858")
      , hex("e4f483"), hex("b4e46c"), hex("b0d858"), hex("a8d048"), hex("78c040")
      , hex("d0e46c"), hex("b4cc54"), hex("a4b448"), hex("809434"), hex("647830")
      , hex("98c45c"), hex("8ca458"), hex("7ca84c"), hex("749838"), hex("607428")
      , hex("d0dc90"), hex("c4cc78"), hex("a4b45c"), hex("748440"), hex("6c7834")
      , hex("c4f280"), hex("90c46c"), hex("64a859"), hex("589c48"), hex("549040")
      , hex("a8f894"), hex("80d88c"), hex("68bc90"), hex("449c64"), hex("40804c")
      , hex("8cf4b4"), hex("70dc98"), hex("50c884"), hex("34bc68"), hex("30a460")
      , hex("90f4d4"), hex("60e0bc"), hex("50d8ac"), hex("40b094"), hex("3cbc84")
      , hex("40e8c8"), hex("34d8b8"), hex("30c0a4"), hex("2ca488"), hex("289080")
      , hex("5ce8ec"), hex("48e0ec"), hex("34c4cc"), hex("2c9cac"), hex("208898")
      , hex("9ce0ec"), hex("80c8e4"), hex("68bcd0"), hex("489cb4"), hex("34889c")
      , hex("4ca8d8"), hex("3c94c8"), hex("3482b0"), hex("2878a8"), hex("20709c")
      , hex("4490e0"), hex("4070d0"), hex("44589c"), hex("3854ac"), hex("344ca8")
      , hex("6c7ca0"), hex("4c6894"), hex("4c5878"), hex("405474"), hex("344c6c")
      , hex("6080f0"), hex("4c64d4"), hex("385ca8"), hex("34488c"), hex("2c447c")
      , hex("98b0ec"), hex("7494e0"), hex("6880f0"), hex("5c63d4"), hex("5450c4")
      , hex("acb0e8"), hex("9490d8"), hex("7464b0"), hex("644494"), hex("644088")
      , hex("bca0f8"), hex("ac80ec"), hex("8c64d8"), hex("7c50c8"), hex("6844ac")
      , hex("c8ace4"), hex("b088dc"), hex("9468ac"), hex("8450a4"), hex("7448bc")
      , hex("eca0f8"), hex("cc84e0"), hex("b86cd0"), hex("a850c0"), hex("9c40ac")
      , hex("e894a0"), hex("e87c94"), hex("dc6c98"), hex("c05c90"), hex("a84884")
      , hex("f49498"), hex("e4908c"), hex("d8747c"), hex("d05874"), hex("c44868")
      , hex("fca8b4"), hex("f898a8"), hex("f47490"), hex("f06084"), hex("e85078")
      , hex("e864a8"), hex("e0549c"), hex("d4488c"), hex("c4387c"), hex("b43078")
      , hex("f6f8ee"), hex("f4f4f8"), hex("f0f0e8"), hex("dcdcd4"), hex("d0ccc4")
      , hex("f8ecec"), hex("f0ecf0"), hex("fcf4f0"), hex("dcd4d8"), hex("d8d0c8")
      , hex("d8d0d0"), hex("d0c0c8"), hex("c0b0b8"), hex("a898a4"), hex("a09098")
      , hex("846c70"), hex("745c64"), hex("6c5864"), hex("5c4c5c"), hex("584858")
      , hex("787084"), hex("6b6474"), hex("645c6c"), hex("544c64"), hex("544460")
      , hex("9c5c4c"), hex("ac6448"), hex("9c5c4c"), hex("884c3c"), hex("784434")
      , hex("784434"), hex("ac784c"), hex("986c44"), hex("885c44"), hex("785038")
      , hex("d8b49c"), hex("c49c80"), hex("a07864"), hex("8c6854"), hex("805c48")
    )

    val SkinColors: List[Color] = List(
            // A
            hex("c07850"), hex("9c6c50"), hex("986040"), hex("a06850"), hex("986040")
                ,hex("9c625a"), hex("906050"), hex("88584c")
            // B
            , hex("b88868"), hex("bc8468"), hex("b07658"), hex("b07650"), hex("bc7044")
                , hex("b07058"), hex("b07458"), hex("a06850"), hex("986048")

            // C
            , hex("f0c898"), hex("dd9564"), hex("e8985e"), hex("906552"), hex("9e7054")
                , hex("e48e45"), hex("b87040"), hex("b47048"), hex("dd8b59")

            // D
            , hex("de9c6a"), hex("ce8f63"), hex("bb7c53"), hex("c08048"), hex("e08b4a")
                , hex("d59452"), hex("c07840")

            // E
            , hex("e4ac7c"), hex("eba464"), hex("e09860"), hex("e89858"), hex("e59454")
                , hex("e08547"), hex("e09838")

            // F
            , hex("f5c781"), hex("eca55e"), hex("eeac5a"), hex("e89850"), hex("e8a656")

            // G
            , hex("f8c888"), hex("f4c19b"), hex("f8c090"), hex("f4ba6b")

            // H
            , hex("f0d0a8"), hex("ecbc9c")

            // I
            , hex("f8c880"), hex("f8c870"), hex("f8d870"), hex("f8d868")

        )

    val HairCVP: List[ColorValPalette] = {
        import ColorValPalette._
        List(
            hairhex("e89088", "b0606c", "78394c"), hairhex("d8847c", "a45058", "6c3440"), hairhex("d8646c", "ac4450", "703038")    
            , hairhex("d45870", "9c3c50", "6c2c44"), hairhex("bc3c58", "84283c", "582038")        

            , hairhex("f08878", "c8585c", "7c3444"), hairhex("f86860", "ac404c", "6c2c30"), hairhex("f06060", "b43848", "6c2834")        
            , hairhex("e84c54", "9c3040", "642830"), hairhex("dc3848", "9c3040", "5c2028")   

            
            , hairhex("ec3848", "9c2c44", "5c283c"), hairhex("e02c44", "90283c", "5c2434"), hairhex("d02838", "842034", "582030")        
            , hairhex("c02034", "842034", "541c2c"), hairhex("b01830", "741c34", "4c182c") 

            , hairhex("f8404c", "ac3444", "682c3c"), hairhex("f03848", "9c3040", "642438"), hairhex("dc2c3c", "882840", "582438")        
            , hairhex("d42434", "90243c", "582038"), hairhex("c42030", "802434", "502038") 

            , hairhex("f83830", "c0241c", "781c24"), hairhex("f8302c", "bc1c10", "741820"), hairhex("f42c24", "b41810", "6c141c")        
            , hairhex("f0241c", "ac140c", "6c1018"), hairhex("ec1c18", "a41008", "601018") 

            , hairhex("d04c38", "a03040", "6c2034"), hairhex("dc3c34", "943844", "5c2440"), hairhex("e83c38", "9c3028", "68201c")        
            , hairhex("dc3034", "842834", "581c2c"), hairhex("e43c28", "ac2834", "6c2834") 

            , hairhex("fc8864", "c45840", "7c342c"), hairhex("f07048", "b44830", "743420"), hairhex("ec6838", "9c4428", "6c3014")        
            , hairhex("e84824", "a8341c", "80241c"), hairhex("ec542c", "b42c20", "801c20") 

            , hairhex("f88c38", "cc6030", "8c4c2c"), hairhex("f48424", "c4602c", "804428"), hairhex("f88028", "c85624", "883a2a")        
            , hairhex("ec6424", "a84828", "703424"), hairhex("e85c1c", "ac401c", "602c1c") 

            , hairhex("fc9450", "c86844", "804028"), hairhex("f8ac60", "c47440", "8c4c2c"), hairhex("f0b058", "b07838", "6c4430")        
            , hairhex("e89c4c", "a46830", "684028"), hairhex("d08838", "986030", "684028") 

            , hairhex("d8a050", "ac6c44", "68443c"), hairhex("cc9448", "9c603c", "603834"), hairhex("c0803c", "905434", "58302c")        
            , hairhex("a86834", "78442c", "4c2824"), hairhex("9c6030", "6c402c", "442424") 

            , hairhex("f4e498", "d4a058", "805c44"), hairhex("e8d888", "c09050", "785840"), hairhex("dcc484", "ac7c50", "74503c")        
            , hairhex("d0b470", "a07448", "6c4c3c"), hairhex("c09c50", "886440", "5c4030") 

            , hairhex("f4f8d0", "e0c090", "a47858"), hairhex("f4f8bc", "e0c088", "9c7450"), hairhex("f0f8bc", "d8c078", "986c48")        
            , hairhex("e4dca8", "c4a864", "846448"), hairhex("e0c490", "b49058", "886040") 

            , hairhex("ecd088", "c89050", "8c5c30"), hairhex("e4d86c", "c09844", "8c5c30"), hairhex("e8d854", "c8a048", "9c6830")        
            , hairhex("d8dc60", "b4983c", "805c24"), hairhex("e0e858", "b49c38", "846424") 

            , hairhex("e4f483", "90b45c", "587444"), hairhex("b4e46c", "5cac64", "3c6844"), hairhex("b0d858", "709840", "546430")        
            , hairhex("a8d048", "5c9840", "386830"), hairhex("78c040", "4e8524", "404c20") 

            , hairhex("d0e46c", "90a448", "547045"), hairhex("b4cc54", "788c44", "505c44"), hairhex("a4b448", "608048", "385034")        
            , hairhex("809434", "50683c", "304024"), hairhex("647830", "445024", "383020") 

            , hairhex("98c45c", "6c8844", "485430"), hairhex("8ca458", "5c7040", "40482c"), hairhex("7ca84c", "506c38", "4c4430")        
            , hairhex("749838", "486034", "443c30"), hairhex("607428", "385028", "383420") 

            , hairhex("d0dc90", "8cac64", "486854"), hairhex("c4cc78", "809854", "3c6048"), hairhex("a4b45c", "607c48", "345044")        
            , hairhex("748440", "405c38", "284044"), hairhex("6c7834", "385830", "283c40") 

            , hairhex("c4f280", "78a858", "406c34"), hairhex("90c46c", "488c50", "24503c"), hairhex("64a859", "407048", "305034")        
            , hairhex("589c48", "346838", "30482c"), hairhex("549040", "386440", "304434") 

            , hairhex("a8f894", "6cc084", "3c7858"), hairhex("80d88c", "589c80", "346458"), hairhex("68bc90", "388064", "2c4850")        
            , hairhex("449c64", "386c50", "284434"), hairhex("40804c", "2c5c4c", "283840") 

            , hairhex("8cf4b4", "50a87c", "386450"), hairhex("70dc98", "3c946c", "2c6048"), hairhex("50c884", "348860", "2c5844")        
            , hairhex("34bc68", "287c50", "245440"), hairhex("30a460", "287450", "1c483c") 

            , hairhex("90f4d4", "58c098", "2c705f"), hairhex("60e0bc", "349c7c", "1c605c"), hairhex("50d8ac", "34907c", "1c4c58")        
            , hairhex("40b094", "347484", "30485c"), hairhex("3cbc84", "2e8070", "244860") 

            , hairhex("40e8c8", "30a0a8", "305074"), hairhex("34d8b8", "2c8894", "304064"), hairhex("30c0a4", "28707c", "303858")        
            , hairhex("2ca488", "246470", "3c3054"), hairhex("289080", "205464", "342c48") 

            , hairhex("5ce8ec", "3c98bc", "445080"), hairhex("48e0ec", "3494ac", "3c4878"), hairhex("34c4cc", "307ca4", "383c68")        
            , hairhex("2c9cac", "24607c", "343058"), hairhex("208898", "24506c", "302c50") 

            , hairhex("9ce0ec", "7498c0", "485c84"), hairhex("80c8e4", "6484b4", "445880"), hairhex("68bcd0", "5078a4", "384870")        
            , hairhex("489cb4", "346488", "383c60"), hairhex("34889c", "305878", "3c3858") 

            , hairhex("4ca8d8", "406ca8", "484074"), hairhex("3c94c8", "386098", "403868"), hairhex("3482b0", "345890", "403860")        
            , hairhex("2878a8", "3c4c88", "403464"), hairhex("20709c", "344480", "402c50") 

            , hairhex("4490e0", "3c60ac", "343c78"), hairhex("4070d0", "344890", "303460"), hairhex("44589c", "403c68", "302854")        
            , hairhex("3854ac", "343880", "28245c"), hairhex("344ca8", "303874", "302858") 

            , hairhex("6c7ca0", "485478", "403c60"), hairhex("4c6894", "404870", "383450"), hairhex("4c5878", "3c405c", "302c44")        
            , hairhex("405474", "343c5c", "302844"), hairhex("344c6c", "303454", "302840") 

            , hairhex("6080f0", "3c58ac", "304478"), hairhex("4c64d4", "304494", "303860"), hairhex("385ca8", "303c6c", "34304c")        
            , hairhex("34488c", "2c3460", "342844"), hairhex("2c447c", "342c58", "302440") 

            , hairhex("98b0ec", "6b78c8", "50448c"), hairhex("7494e0", "505cb4", "3f3280"), hairhex("6880f0", "504cb8", "362f78")        
            , hairhex("5c63d4", "443b8c", "2b2760"), hairhex("5450c4", "3b3280", "2f2258") 

            , hairhex("acb0e8", "907cbc", "644c84"), hairhex("9490d8", "745c9c", "4c4070"), hairhex("7464b0", "5c4090", "483068")        
            , hairhex("644494", "4c3074", "342454"), hairhex("644088", "443464", "3c2848") 

            , hairhex("bca0f8", "946cc0", "64447c"), hairhex("ac80ec", "8858bc", "58346c"), hairhex("8c64d8", "7444a0", "4c2c60")        
            , hairhex("7c50c8", "603884", "442450"), hairhex("6844ac", "4c3870", "3c2848") 

            , hairhex("c8ace4", "9880c8", "68588c"), hairhex("b088dc", "7860a4", "583c6c"), hairhex("9468ac", "68447c", "483064")        
            , hairhex("8450a4", "5c3c80", "482c60"), hairhex("7448bc", "603884", "442860") 

            , hairhex("eca0f8", "ac68bc", "704484"), hairhex("cc84e0", "985ca4", "683c74"), hairhex("b86cd0", "884894", "603068")        
            , hairhex("a850c0", "783c84", "542c5c"), hairhex("9c40ac", "703878", "502848") 

            , hairhex("e894a0", "cc6078", "943050"), hairhex("e87c94", "cc5870", "843849"), hairhex("dc6c98", "a8486c", "743460")        
            , hairhex("c05c90", "983c84", "6c3050"), hairhex("a84884", "843874", "502848") 

            , hairhex("f49498", "d0586c", "8c3448"), hairhex("e4908c", "c05464", "7c3044"), hairhex("d8747c", "a0445c", "642c3c")        
            , hairhex("d05874", "8c3c58", "643040"), hairhex("c44868", "90344c", "58283c") 

            , hairhex("fca8b4", "f86c9c", "a83c6c"), hairhex("f898a8", "ec5c88", "9c3860"), hairhex("f47490", "d04470", "843050")        
            , hairhex("f06084", "c33864", "6c2c48"), hairhex("e85078", "ac2c58", "602c44") 

            , hairhex("e864a8", "ac346c", "683054"), hairhex("e0549c", "a03464", "602c50"), hairhex("d4488c", "942c58", "5c2448")        
            , hairhex("c4387c", "882850", "582048"), hairhex("b43078", "70284c", "481c40") 

            , hairhex("f6f8ee", "ccc4c0", "949494"), hairhex("f4f4f8", "c0bcc4", "888084"), hairhex("f0f0e8", "c4c0c0", "848088")        
            , hairhex("dcdcd4", "a0a8a4", "6c6c70"), hairhex("d0ccc4", "90948c", "646060") 

            , hairhex("f8ecec", "c8b8bc", "94888c"), hairhex("f0ecf0", "c4bcc0", "807474"), hairhex("fcf4f0", "c4bcac", "7c746c")        
            , hairhex("dcd4d8", "a09ca4", "70686c"), hairhex("d8d0c8", "949088", "64605c") 

            , hairhex("d8d0d0", "a894a4", "6c5864"), hairhex("d0c0c8", "988494", "604c58"), hairhex("c0b0b8", "847484", "50404c")        
            , hairhex("a898a4", "6c5c68", "483444"), hairhex("a09098", "645060", "442c40") 

            , hairhex("846c70", "5c4850", "443038"), hairhex("745c64", "58404c", "402c38"), hairhex("6c5864", "543c4c", "402c38")        
            , hairhex("5c4c5c", "403444", "342834"), hairhex("584858", "403440", "342434") 

            , hairhex("787084", "544c64", "44384c"), hairhex("6b6474", "484458", "383044"), hairhex("645c6c", "4c4058", "3c2c44")        
            , hairhex("544c64", "443450", "38243c"), hairhex("544460", "443048", "3c243c") 

            , hairhex("9c5c4c", "744844", "583438"), hairhex("ac6448", "7c4c44", "5e383c")       
            , hairhex("884c3c", "60383c", "4c2c30"), hairhex("784434", "5c3438", "48282c") 

            , hairhex("b47c40", "885c3c", "64443c"), hairhex("ac784c", "805448", "603c40"), hairhex("986c44", "744c34", "58342c")        
            , hairhex("885c44", "604038", "4c3034"), hairhex("785038", "5c3c34", "482c28") 

            , hairhex("d8b49c", "a47c70", "643c4c"), hairhex("c49c80", "84645c", "58384c"), hairhex("a07864", "6c5048", "503444")        
            , hairhex("8c6854", "5c403c", "482838"), hairhex("805c48", "583c38", "402430")     
        )
    }

    val SkinCVP: List[ColorValPalette] = {
        import ColorValPalette._

        List(
            // ephraim
        DEFAULT_SKIN_COLOR
            // A
        ,  skin("e8a474", "c07850", "985838", "744428", "483020"),  skin("c48860", "9c6c50", "805044", "604438", "483038") 
        ,  skin("b47c58", "986040", "784038", "583030", "403028") ,  skin("b47858", "986040", "784038", "583030", "403028") 
        ,  skin("c88c60", "a06850", "805044", "644838", "4e363a") ,  skin("b47850", "986040", "784038", "583030", "403028") 
        ,  skin("b47b62", "9c625a", "834a4a", "633f46", "503140") ,  skin("a8786c", "906050", "785040", "604438", "483038") 
        ,  skin("9c7464", "88584c", "704834", "583430", "40302c")
            // B 
        ,  skin("e3a873", "b88868", "a36b5b", "81535a", "603c48") ,  skin("e4a874", "bc8468", "9c6054", "7c4c54", "603c48") 
        ,  skin("d09264", "b07658", "8c5341", "704a3e", "4e363a") ,  skin("d09660", "b07650", "8c5341", "704a3e", "4e363a") 
        ,  skin("dc9764", "bc7044", "905430", "78402a", "442a24") ,  skin("d89060", "b07058", "8c5341", "704a3e", "4e363a") 
        ,  skin("e09860", "b07458", "885444", "644838", "4e363a") ,  skin("d08c58", "a06850", "805040", "604438", "483038") 
        ,  skin("a87c60", "986048", "784c40", "604438", "483038") 
            // C
        ,  skin("f8f0e8", "f0c898", "e8a078", "987058", "604848"),  skin("f8c089", "dd9564", "c1774a", "8a5548", "533e3e")
        ,  skin("f9b980", "e8985e", "b56a3e", "8a4e39", "554040"),  skin("b08157", "906552", "7d4f46", "633f46", "493637")
        ,  skin("bc8658", "906552", "7d4f46", "633f46", "483432"),  skin("c28a5a", "9e7054", "885648", "664640", "4e363a")
        ,  skin("f8ac68", "e48e45", "b1652e", "803a24", "402a30"),  skin("d09058", "b87040", "985828", "704028", "503020")
        ,  skin("503020", "b47048", "985838", "744428", "483020"),  skin("efaa65", "dd8b59", "b1654b", "824842", "463140")
            // D
        ,  skin("f8cd9c", "de9c6a", "a86840", "805840", "604840")
        ,  skin("e9b788", "ce8f63", "a76a4b", "74453b", "523036")
        ,  skin("d5a56d", "bb7c53", "9a6143", "754d3a", "513b3d")
        ,  skin("e8b070", "c08048", "a86840", "805840", "604840")
        ,  skin("f5bd73", "e08b4a", "a86a41", "835229", "4d3738")
        ,  skin("ffc573", "d59452", "ac7339", "835a38", "604848")
        ,  skin("d8a058", "c07840", "985838", "784830", "583828")

            // E
        ,  skin("f4dcb4", "e4ac7c", "c67a50", "906040", "604848")
        ,  skin("f7d79e", "eba464", "c17d4a", "8a6139", "5d4343")
        ,  skin("e8c088", "e09860", "b07840", "805840", "584040")
        ,  skin("f8d088", "e89858", "b87848", "886038", "604848")
        ,  skin("f7cb82", "e59454", "b47446", "865c35", "5c4444")
        ,  skin("f5c776", "e08547", "a55f3a", "785033", "4d3738")
        ,  skin("f8c060", "e09838", "c07830", "986038", "604038")

            // F
        ,  skin("fde8bb", "f5c781", "e89858", "9d6340", "604848")
        ,  skin("fad385", "eca55e", "c08045", "936142", "56413f")
        ,  skin("f8de8b", "eeac5a", "cd7b4a", "885a41", "604848")
        ,  skin("f8d088", "e89850", "b87848", "886038", "604848")
        ,  skin("fad085", "e8a656", "bf823d", "8f592a", "604848")       

            // G
        ,  skin("f8f0d8", "f8c888", "e89858", "987048", "604848")
        ,  skin("f8f0d8", "f4c19b", "dc8965", "a45b59", "583f50")
        ,  skin("f8e8c0", "f8c090", "e88c58", "986848", "604848")
        ,  skin("ffe19a", "f4ba6b", "d98857", "9c7757", "715757")

            // H
        ,  skin("f8f0d0", "f0d0a8", "f8a878", "b87050", "705048")
        ,  skin("f8e4c4", "ecbc9c", "d08870", "9e6058", "5c3c48") 

            // I
        ,  skin("f8f8c8", "f8c880", "e89860", "987048", "604848")
        ,  skin("f8f8b0", "f8c870", "e09058", "987048", "604848")
        ,  skin("f8f8b8", "f8d870", "e89850", "987048", "604848")
        ,  skin("f8f8b8", "f8d868", "f0a850", "987048", "604848")
        )

    }








}
