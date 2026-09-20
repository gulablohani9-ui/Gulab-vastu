# Gulab Vastu Chakra — GitHub APK Project

यह project दिए गए `Copy of 6 points 12-1-24.ggb` को सीधे app में preload करता है। GeoGebra construction को बदलने के बजाय उसी `.ggb` file को इस्तेमाल किया गया है।

## क्या मिलेगा
- Original GGB construction preload
- Mobile GeoGebra toolbar और draggable/editable objects
- `📷 Map` button से phone की photo चुनकर GeoGebra में image insert
- Photo को drag/resize करके map align करना
- Polygon/point tools के लिए GeoGebra का original तरीका
- Original construction reset
- Current construction को `.ggb` में save करने का button
- GitHub Actions से APK automatically build

## GitHub पर कैसे चलाना है
1. इस पूरे folder की files GitHub repository में उसी structure में upload करें।
2. `Actions` खोलें।
3. `Build APK` workflow चुनें।
4. `Run workflow` दबाएँ।
5. Build complete होने पर नीचे `Artifacts` में `Gulab-Vastu-Chakra-debug` ZIP मिलेगा।
6. ZIP खोलकर `app-debug.apk` phone में install करें।

## जरूरी बात
GeoGebra engine अभी official GeoGebra web runtime से load होता है, इसलिए app को पहली बार/चलाते समय internet चाहिए। Original `.ggb` construction project में local asset के रूप में भी रखा गया है।

## Source
GeoGebra Apps API supports loading a `.ggb` as base64 and inserting images through its API. See official documentation: https://geogebra.github.io/docs/reference/en/GeoGebra_Apps_API/
