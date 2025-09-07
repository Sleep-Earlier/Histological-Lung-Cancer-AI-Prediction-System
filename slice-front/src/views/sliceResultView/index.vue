<!-- src/views/About.vue -->
<template>
  <!-- 上传切片的对话框 -->
  <el-dialog :title="'上传切片'" v-model="isDialogVisible" width="35%" @open="handleDialogOpen"
    :before-close="handleDialogClose" style="padding: 40px;">
    <!-- 表单 -->
    <el-form ref="uploadForm" :model="uploadForm" :rules="rules" label-width="120px" >
      <el-form-item label="文件路径" prop="path">
        <el-input v-model="uploadForm.path" placeholder="请输入svs所处文件路径，如 C:\svs\" />
      </el-form-item>
      <!-- 上传切片文件 -->
      <!-- <el-form-item label="上传切片文件" prop="fileList">
        <el-upload drag action="#" :limit="1" :file-list="uploadForm.fileList" :auto-upload="false"
          :on-change="handleChange" :on-remove="handleFileRemove" :on-exceed="exceedFile">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传svs/png/jpg文件</div>
        </el-upload>
      </el-form-item> -->
    </el-form>
    <!-- 对话框底部按钮 -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="isDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="isUploading">提交</el-button>
    </span>
  </el-dialog>

  <el-row :gutter="10" class="content">
    <!--  左侧切片信息展示界面  -->
    <el-col :span="6">
      <div>
        <!--   信息展示   -->
        <div style="position: relative; width: 100%;height: 80px;display: flex; justify-content: center; align-items: center;">
          <img src="/title-back.png" style="position: absolute; top:0; left:0; width: 100%; height: 80px; opacity: 0.8;"  alt="">
          <span style=" text-align: center;position: relative; z-index: 1; font-size: 26px; font-weight: 800;color: white;margin-left: 20px;margin-top: 8px;">
            {{ itemTitle }}
          </span>
        </div>
        <h4 style="margin-top: 20px;">诊断结果:</h4>
        <div class="content1">
            <h3>恶性</h3>
            <p>高级别上皮内瘤变 (86.20%)</p>
          <img src="/image.png" class="content1-right"  alt="">
        </div>
        <div style="display: inline-flex; align-items: center; justify-content: space-between; width: 100%; padding-right: 10px; margin-top: 10px;">
          <h4 >切片列表:</h4>
          <div v-if="!isExporting">
            <el-button type="primary" @click="isExporting = !isExporting">导出切片</el-button>
            <el-button type="primary" @click="openUploadDialog">上传切片</el-button>
          </div>
          <div v-else>
            <el-button type="danger"  @click="isExporting = !isExporting" plain>&nbsp;&nbsp;退出&nbsp;&nbsp;</el-button>
            <el-button type="success" @click="openUploadDialog">&nbsp;&nbsp;导出&nbsp;&nbsp;</el-button>
          </div>
        </div>

        <!--   切片列表    -->
        <div style="max-height:calc( 100vh - 450px );margin-bottom: 5px; overflow: auto; margin-left: 20px; margin-right: 10px;" >
          <el-checkbox-group>
            <el-checkbox v-for="(item, index) in cardData" :key="index" :value="index" class="slice-checkbox">
              <el-card :key="index" class="clickable-card" shadow="never" style="height: 100px"
                       :class="{'clickable-card-active': index === nowItem}" @click="nowItem = index; fetchOneSliceData(cardData[index].id)">
                <el-row :gutter="20" >
                  <el-col :span="6" style="">
                    <img src="/overview.jpg" style="width: 100%; height: 100%;"  alt=""/>
                  </el-col>
                  <el-col :span="18">
                    <div style="display: flex; flex-direction: column; align-items: flex-start; justify-content: space-between">
                      切片: {{ item.name }} <br />
                      上传时间：{{ item.uploadTime }} <br />
                      置信度：{{ item.incidence.toFixed(4) * 100 + "%" || '暂无' }}
                    </div>
                    解析进度:
                    <el-progress
                        :percentage="(item.slicetqdm*100).toFixed(1)"
                        :stroke-width="15"
                        striped
                        striped-flow
                        :duration="10"
                        color="#e6a23c"
                    />
                  </el-col>
                </el-row>
              </el-card>
            </el-checkbox>
          </el-checkbox-group>
        </div>

        <!--     分页     -->
        <div style="text-align: center;">
          <div style="display: inline-block;">
            <el-pagination
            background
            layout="prev, pager, next"
            :total="totalItems"
            :page-size="pageSize"
            @current-change="handlePageChange"
            style="margin-top: 20px;  "
          />
          </div>
        </div>
      </div>
    </el-col>
    <!--   右侧切片展示界面    -->
    <el-col :span="18" class="slice-card">
      <el-card style="height: calc( 100vh - 90px );" body-style="height: 90%; width: 100%;"
               v-loading="loadingData !== 'None'" :element-loading-text="loadingText">
        <template #header >
          <div style="display: flex; align-items: center; justify-content: space-between">
            <div>
              <el-button :type="tools[0] ? 'warning' : ''" @click="closeAnno"><i-ion-move />&nbsp;移动</el-button>
              <el-button :type="tools[1] ? 'warning' : ''" @click="AnnoRectangle"><i-mynaui-rectangle width="20px" height="20px"/>&nbsp;矩形</el-button>
              <el-button :type="tools[2] ? 'warning' : ''" @click="AnnoPolygon"><i-gis-polygon-o width="24px" height="24px"/>&nbsp;多边形</el-button>
              <el-button :type="tools[3] ? 'warning' : ''" @click="AnnoEllipse"><i-mdi-ellipse-outline width="24px" height="24px"/>&nbsp;椭圆</el-button>
              <!--          <el-button @click="" :disabled="selectedAnno === null"><i-mdi-comment-outline width="24px" height="24px"/>&nbsp;批注</el-button>-->
              <el-button type="danger" @click="deleteAnno" plain :disabled="selectedAnno === null"><el-icon><Delete /></el-icon>&nbsp;删除</el-button>
            </div>
            <div>
              <el-button :type="curveVisible ? 'success' : ''" @click="toggleCurve">轮廓线</el-button>
              <el-button :type="heatMapVisible ? 'success' : ''" @click="toggleHeatMap">热力图</el-button>
            </div>
          </div>
        </template>
        <div style="height:100%; width: 100%;">
          <div id="open-sea-dragon" style="height:100%; width: 100%; border: none !important;"></div>
        </div>
      </el-card>

      <el-card id="comment-card" v-show="commentVisible">
        <el-text style="margin-bottom: 10px">批注：</el-text>
        <el-input v-model="commentValue"></el-input>
      </el-card>

<!--      <span ref="comment_trigger" id="comment" style="position: absolute; top: 0; left: -40px; width: 20px; height: 20px; border: black 1px solid"></span>-->
<!--      <el-popover ref="popover" :virtual-ref="comment_trigger" placement="bottom" :width="200"-->
<!--                  :visible="comment_visible">-->
<!--        <div>···内容···</div>-->
<!--        <el-input></el-input>-->
<!--      </el-popover>-->
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
  import OpenSeadragon from "openseadragon"
  import {Annotation, createOSDAnnotator} from '@annotorious/openseadragon'
  import {mountPlugin as mountToolsPlugin} from "@annotorious/plugin-tools"
  // @ts-ignore
  import { enableGeoTIFFTileSource } from "geotiff-tilesource";
  import '@annotorious/openseadragon/annotorious-openseadragon.css'
  import {computed, onMounted, ref} from "vue";
  import {Delete} from "@element-plus/icons-vue";
  import {useStore} from 'vuex';
  import {v4 as uuid} from "uuid"
  import h337 from "heatmap.js"
  import colormap from "colormap"
  // @ts-ignore
  import {getSliceListBackEnd, getSliceResult, getCurveResult, getHeatMapResult} from '@/api/slice';
  import {MessageHandler} from "element-plus";

  // 全局参数
  const loadingData = ref("None")
  const loadingText = computed(() => {
    if (loadingData.value == "Slice")
      return "正在获取切片数据"
    else if (loadingData.value == "Curve")
      return "正在获取轮廓线数据"
    else if (loadingData.value == "HeatMap")
      return "正在获取热力图数据"
    else
      return "正在加载数据"
  })

  onMounted(() => {
    initOpenSeadragon()
    initAnno()
    fetchCardData(false)//第一次获取数据
    intervalId.value = setInterval(() => {
      fetchCardData(true);  // 传递参数调用 fetchCardData
    }, 5000)
    // 某条件下停止
    // setTimeout(() => {
    //   clearInterval(intervalId);
    //   console.log("停止了定时执行");
    // }, 30000); // 30秒后停止执行
  })

  // OpenSeaDragon 初始化
  const intervalId= ref()
  const options = ref()
  const viewer = ref()
  const zoomNumber = ref("1")

  const initOpenSeadragon = () => {
    enableGeoTIFFTileSource(OpenSeadragon)
    // 配置详情见 https://blog.csdn.net/qq_30014557/article/details/123467521
    options.value = {
      id: "open-sea-dragon",
      prefixUrl: "https://cdn.jsdelivr.net/gh/Benomrans/openseadragon-icons@main/images/",
      gestureSettingsMouse:{
        clickToZoom: false,
        dblClickToZoom: true
      },
      navigationControlAnchor: 'TOP_RIGHT',
      showRotationControl: true,
      showFlipControl: true,
      visibilityRatio: 1,
      constrainDuringPan: true,
      defaultZoomLevel: 1.0,

      showNavigator:  true,
      navigatorMaintainSizeRatio: true,
      navigatorSizeRatio: 0.22,
      navigatorOpacity: 0.8,
      navigatorAutoFade: true,
      controlsFadeDelay: 2000,
      navigatorRotate:true,
      navigatorBorderColor: '#0074f8',
      navigatorDisplayRegionColor: '#4e8e2f',
      renderers: {
        type: 'webgl',
        options: {
          preserveDrawingBuffer: true
        }
      }
    }
    viewer.value = OpenSeadragon(options.value)
    viewer.value.addHandler("pan", () => {
      if (selectedElement.value && updatePos.value)
        setComment()
    })
    viewer.value.addHandler("zoom", (zoomObj: {zoom: number}) => {
      zoomNumber.value = zoomObj.zoom.toFixed(2)
      if (selectedElement.value && updatePos.value)
        setComment()
    })
  }

  // 轮廓线初始化、轮廓线绘制功能函数
  const anno = ref(null)
  const tools = ref([true, false, false, false])  // move, rectangle, polygon
  const selectedAnno = ref(null)
  const selectedElement = ref(null)

  const initAnno = () => {
    // https://annotorious.dev/api-reference/events/#selectionchanged
    anno.value = createOSDAnnotator(viewer.value, {
      drawingEnabled: false,
      autoSave: true,
    })

    anno.value.setStyle((annotation: Annotation,
                         state: {selected: boolean, hovered: boolean}) => {
      let opacity = 0
      if (state && state.selected)
        opacity = 0.3
      else if (state && state.hovered)
        opacity = 0.15

      return {
        fill: "#00ff00",
        fillOpacity: opacity,
        stroke: '#00ff00',
        strokeOpacity: 1
      }
    });

    anno.value.on('selectionChanged', (annotations: Annotation[]) => {
      if (annotations.length === 0) {
        const tmp = anno.value.getAnnotationById(selectedAnno.value)
        if (tmp.bodies.length !== 0) {
          tmp.bodies[0].value = commentValue.value
          anno.value.updateAnnotation(tmp)
        }
        selectedAnno.value = ''
        commentVisible.value = false
      }
      else {
        selectedAnno.value = annotations[0].id
        selectedElement.value = document.getElementsByClassName("a9s-annotation selected")[0]
        commentValue.value = anno.value.getAnnotationById(selectedAnno.value).bodies[0].value
        commentVisible.value = true
        setComment()
      }
    });

    anno.value.on('clickAnnotation', (annotation: Annotation, originalEvent: PointerEvent) => {
      // if (annotation.target.creator?.id === "auto-display")
      //   anno.value.setUserSelectAction(UserSelectAction.SELECT)
      // else
      //   anno.value.setUserSelectAction(UserSelectAction.EDIT)
    });

    anno.value.on('createAnnotation', (annotation: Annotation) => {
      console.log("Annotation created: ")
      annotation.bodies.push({
        id: uuid(),
        annotation: annotation.id,
        purpose: 'commenting',
        value: ''
      })
    });

    anno.value.on('updateAnnotation', ((updated: Annotation, previous: Annotation) => {
      console.log('Annotation was updated.');
      setComment()
    }))

    mountToolsPlugin(anno.value)
  }

  const closeAnno = (): void => {
    viewer.value.panHorizontal = true
    viewer.value.panVertical = true
    anno.value.setDrawingEnabled(false)
    tools.value = [true, false, false, false]
  }

  const AnnoRectangle = (): void => {
    viewer.value.panHorizontal = false
    viewer.value.panVertical = false
    anno.value.setDrawingEnabled(true)
    anno.value.setDrawingTool("rectangle")
    tools.value = [false, true, false, false]
  }

  const AnnoPolygon = (): void => {
    viewer.value.panHorizontal = false
    viewer.value.panVertical = false
    anno.value.setDrawingEnabled(true)
    anno.value.setDrawingTool("polygon")
    tools.value = [false, false, true, false]
  }

  const AnnoEllipse = (): void => {
    viewer.value.panHorizontal = false
    viewer.value.panVertical = false
    anno.value.setDrawingEnabled(true)
    anno.value.setDrawingTool("ellipse")
    tools.value = [false, false, false, true]
  }

  const deleteAnno = (): void => {
    if (selectedAnno.value !== '')
      anno.value.removeAnnotation(selectedAnno.value)
    selectedAnno.value = ''
  }

  const getNewAnnoTemplate = () => {
    return {
      id: uuid(),
      target: {
        selector: {
          type: "POLYGON",
          geometry: {
            bounds: {},
            points: []
          }
        },
        creator: {
          id: "auto-display",
          isGuest: true,
        },
      }
    };
  }

  // 轮廓线加载与展示
  const curveVisible = ref(false)
  const curveData = ref(undefined)

  const toggleCurve = async () => {
    loadingData.value = "Curve"
    curveVisible.value = !curveVisible.value

    if (curveVisible.value) {
      anno.value.clearAnnotations()
      await fetchCurveData(cardData.value[nowItem.value].id).then(() => loadAnno(curveData.value))
    } else {
      anno.value.clearAnnotations()
    }

    loadingData.value = "None"
  }

  const fetchCurveData = async (sliceId: number) => {
    if (curveData.value === undefined)
      await getCurveResult(sliceId).then((res: any) =>
        curveData.value = res.data
      )
  }

  const loadAnno = (data: any): void => {
    anno.value.clearAnnotations()
    // 获取 DZI 图像的尺寸
    const dziWidth = SliceData.value.dzi.width;
    const dziHeight = SliceData.value.dzi.height;

    // 获取原 curve 的列数和行数
    const curveCols = data.curveCols;
    const curveRows = data.curveRows;
    // 将 curve 坐标映射到 DZI 图像的坐标系
    const mapToDziCoordinates = (x, y) => {
      return {
        x: (x / curveCols) * dziWidth,
        y: (y / curveRows) * dziHeight,
      };
    };

    data.pointList.forEach((point: number[][], index: number) => {
      const newAnno = getNewAnnoTemplate()
      // 遍历 points 列表并转换每个点的坐标
      newAnno.target.selector.geometry.points = point.map(([x, y]) => {
        const mappedPoint = mapToDziCoordinates(x, y);
        return [mappedPoint.x, mappedPoint.y];
      })
      const values = data.max_min_values[index]
      // 将每个值转换为数字
      data.max_min_values[index].minY = parseFloat(values.minY);
      data.max_min_values[index].minX = parseFloat(values.minX);
      data.max_min_values[index].maxY = parseFloat(values.maxY);
      data.max_min_values[index].maxX = parseFloat(values.maxX);
      // 使用映射后的坐标更新 min/max 值
      const minCoords = mapToDziCoordinates(values.minX, values.minY);
      const maxCoords = mapToDziCoordinates(values.maxX, values.maxY);
      data.max_min_values[index].minX = minCoords.x;
      data.max_min_values[index].minY = minCoords.y;
      data.max_min_values[index].maxX = maxCoords.x;
      data.max_min_values[index].maxY = maxCoords.y;

      newAnno.target.selector.geometry.bounds = data.max_min_values[index]
      anno.value.addAnnotation(newAnno)
    })
  }

  // 热力图加载与展示
  const heatMapVisible = ref(false)
  const heatMapData = ref(undefined)

  const toggleHeatMap = () => {
    loadingData.value = "HeatMap"
    heatMapVisible.value = !heatMapVisible.value;
    const overlayElement = document.getElementById('heatmap-overlay');

    if (overlayElement) {
      // 使用 requestAnimationFrame 来更新显示状态
      requestAnimationFrame(() => {
        overlayElement.style.opacity = heatMapVisible.value ? '1' : '0'
        if (heatMapVisible.value) {
          overlayElement.style.width = overlayElement.offsetWidth + 1 + 'px'   // 强制触发 resize 变化
          overlayElement.style.height = overlayElement.offsetHeight + 1 + 'px' // 改变大小
        } else {
          overlayElement.style.width = overlayElement.offsetWidth - 1 + 'px'   // 强制触发 resize 变化
          overlayElement.style.height = overlayElement.offsetHeight - 1 + 'px' // 改变大小
        }
      })
    } else {
        fetchHeatMapData(cardData.value[nowItem.value].id).then(() =>
        loadHeatMap()
      )
    }

    loadingData.value = "None"
  }

  const getHeatMapColor = () => {
    const colors = {}
    let n = 1
    colormap({
      colormap: 'jet',
      nshades: 10,
      format: 'hex',
      alpha: 1
    }).forEach((color: string) => {
      colors[(n++ / 10).toFixed(2).toString()] = color
    })
    return colors
  }

  const fetchHeatMapData = async (sliceId: number) => {
    if (heatMapData.value === undefined) {
      await getHeatMapResult(sliceId).then((res: any) =>
        heatMapData.value = res.data
      ).catch((error: any) =>
        ElMessage.error("热力图加载失败")
      )
    }
  }

  const loadHeatMap = (): void => {
    const overlay = {
      width: 1,
      height:  parseInt(heatMapData.value.heatmapRows) / parseInt(heatMapData.value.heatmapCols) ,
      element: document.createElement('div'),
    }
    overlay.element.style.padding = '0px';
    overlay.element.id = 'heatmap-overlay';
    viewer.value.addOverlay(overlay);

    const startWidth = overlay.element.offsetWidth;
    const startHeight = overlay.element.offsetHeight;
    const heatmapWidth = heatMapData.value.heatmapCols
    const heatmapHeight =  heatMapData.value.heatmapRows
    const heatmapMatrixData = JSON.parse(heatMapData.value.heatmapData)

    let maxV = 0
    for (let item of heatmapMatrixData) {
      item.x = Math.floor((item.x / heatmapWidth) * startWidth);
      item.y = Math.floor((item.y / heatmapHeight) * startHeight);

      maxV = Math.max(maxV, item.value);
    }

    const heatmapInstance = h337.create({
      container: overlay.element,
      gradient: getHeatMapColor(),
      radius: 10.5 * parseFloat(zoomNumber.value),  // 乘上缩放倍数, 这样在任何时候渲染都不会导致颜色太淡
      blur: .75,
      maxOpacity: 0.8,
      minOpacity: 0,
    });

    heatmapInstance.setData({
      max: maxV,
      min: 0,
      data: heatmapMatrixData,
    })

    heatmapInstance.repaint()

    const resizeObserver = new ResizeObserver(function (entries) {
      const canvasElement = document.getElementsByClassName('heatmap-canvas')[0] as HTMLCanvasElement;
      if (canvasElement) {
        canvasElement.style.transform = `scale(${entries[0].contentRect.width / startWidth})`
        canvasElement.style.transformOrigin = '0 0'
        canvasElement.style.opacity = "0.5"
        canvasElement.style.filter = 'blur(1px)';
      }
    });

    resizeObserver.observe(overlay.element);
    overlay.element.style.opacity = "1";
  }

  // 批注展示
  const commentVisible = ref(false)
  const commentValue = ref()
  const updatePos = ref(true)
  const comment_trigger = ref()
  const popover = ref()

  const setComment = () => {
    if (selectedElement.value === null)
      return
    // comment_trigger.value.style.top = `${selectedElement.value.getBoundingClientRect().bottom - 30}px`;
    // comment_trigger.value.style.left = `${selectedElement.value.getBoundingClientRect().left + selectedElement.value.getBoundingClientRect().width / 2}px`;
    // popover.value.popperRef?.popperInstanceRef?.update()

    const comment = document.getElementById("comment-card")
    comment.style.transform = `translate(${selectedElement.value.getBoundingClientRect().left + selectedElement.value.getBoundingClientRect().width / 3}px,
                                   ${selectedElement.value.getBoundingClientRect().bottom - 10}px)`
    }

  // 分页相关数据
  // 使用 Vuex getter 获取 appId
  // 获取 Vuex store 实例
  const store = useStore();
  const appId = computed(() => store.getters['appPage/getAppId']);
  const pageNum = ref(1);
  const pageSize = ref(3);
  const totalItems = ref(0);

  // 页面数据
  const cardData = ref([]);
  const SliceData = ref(null)
  const nowItem = ref(0)

  // 获取分页数据的函数
  const fetchCardData = async (interval: boolean = false) => {
    let loadingMessage: MessageHandler
    if (!interval) {
      loadingMessage = ElMessage({
        message: '正在获取切片列表数据，请稍候...',
        type: 'info',
        duration: 0, // 设置为 0 表示消息不会自动关闭
      });
    }
    try {
        const params = {
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          applicationId: 1, // 假设有 applicationId 参数
        };
        // 使用封装的 getSliceList 函数进行请求
        const response = await getSliceListBackEnd(params);
        const data = response.data;
        cardData.value = data.list;    // 接口返回的列表数据
        totalItems.value = data.total; // 接口返回的总条目数
        if (!interval){
          loadingMessage.close();
          await fetchOneSliceData(data.list[0].id)
        }
      }
    catch (error) {
      if (!interval){
        loadingMessage.close();
      }
      console.error('轮询请求出错:', error);
    }
  };

  const openTiff = async (Url: string) => {
    viewer.value.close()
    await OpenSeadragon.GeoTIFFTileSource.getAllTileSources(Url)
        .then((data: any) => {
          viewer.value.open(data[0])
    })
  }

  // 获取后端单个切片数据
  const fetchOneSliceData = async (sliceId: number) =>{
    loadingData.value = "Slice";
    viewer.value.close()
      // 弹出“正在获取数据”提示
    const loadingMessage = ElMessage({
      message: '正在获取切片图像数据，请稍候...',
      type: 'info',
      duration: 0, // 设置为 0 表示消息不会自动关闭
    })

    try {
      const params = {
        sliceId: sliceId,
      };
      // 使用封装的 getSliceList 函数进行请求
      const response = await getSliceResult(params);
      SliceData.value = response.data
      // 解析dzi文件,我放于 index.dzi
      const dziFilePath = SliceData.value.analysisResult + '/index.dzi'
      const xmlText = await fetch(dziFilePath).then(res => res.text())
       // 使用 DOMParser 将 XML 转换为 XMLDocument
      const parser = new DOMParser()
      const xmlDoc = parser.parseFromString(xmlText, 'application/xml')
      // 从 XML 文件中提取信息
      const imageElement = xmlDoc.querySelector('Image')
      const sizeElement = xmlDoc.querySelector('Size')
      let width =  '0';
      let height =  '0';

      if (!imageElement || !sizeElement) {
        console.error('XML 文件格式错误');
        return;
      }
      enableGeoTIFFTileSource(OpenSeadragon)
      viewer.value.open({
        Image: {
          xmlns: "https://schemas.microsoft.com/deepzoom/2008", // 命名空间
          Url: SliceData.value.analysisResult + '/', // 瓦片图加载路径
          Overlap: imageElement.getAttribute('Overlap') || '1',     // 瓦片间重叠像素数
          TileSize: imageElement.getAttribute('TileSize') || '254', // 单瓦片尺寸
          Format: imageElement.getAttribute('Format') || 'jpeg',    // 瓦片格式
          Size: {                                                                      // 大图尺寸
            Width: width = sizeElement.getAttribute('Width') || '0',       // 宽度
            Height: height = sizeElement.getAttribute('Height') || '0'     // 高度
          }
        }
      })

      // console.log("viewer",viewer.value)
      // console.log("viewer遍历",Object.keys(viewer.value));
      // console.log("viewer.value.TileSource",viewer.value.tileSources[0]);
      // Object.keys(viewer.value).forEach(key => {
      //   console.log(`${key}:`, viewer.value[key]);
      // });
      // await openTiff("http://localhost:8083/slices/Test.tiff")

      if (!SliceData.value.dzi) {
        SliceData.value.dzi = {};
      }
      SliceData.value.dzi.width = width;
      SliceData.value.dzi.height = height;

      heatMapVisible.value = false
      curveVisible.value = false
      curveData.value = undefined

      loadingData.value = "None"
      loadingMessage.close();
    } catch (error) {
      console.error('请求出错:', error);
      loadingMessage.close();
    }
  }

  // 页码变化时的处理函数
  const handlePageChange = (newPage: number): void => {
    pageNum.value = newPage
    nowItem.value = 0
    fetchCardData(false) // 重新获取数据
  }

  // 导出切片处理
  const isExporting = ref(false)

</script>

<style scoped>
.content1{
  position: relative;
  background-color: #1141a222;
  margin-left: 20px;
  margin-right: 10px;
  height: 100px;
  text-align: center; /* 让内容居中 */
}
.content1-right{
  position: absolute;
  right: 10px;
  top: 10px;
  height: 80px;

}
.content1 h3 {
  /* position: absolute;
  top: 20px;
  left: 45%;
  font-size: 25px; */
  font-size: 26px;
  color: #1141a2; /* 蓝色标题 */
  margin: 0;
  font-weight: bold;
  padding-top: 23px;
  padding-bottom: 1px;
}

.content1 p {
  /* position: absolute;
  top: 52px;
  left: 27%; */
  max-width: 75%; /* 段落宽度为父容器的 50% */
  font-size: 17px;
  color: #333; /* 字体颜色 */
  margin: 0 auto; /* 让段落水平居中 */
}

.content {
  height: calc(100vh - 55px);
  width: 100vw;
  padding: 15px 10px 10px;
  background-color:#eff3f636 ;

}
.content h4{
  font-size: 23px;
  color: #1141a2; /* 蓝色标题 */
  font-weight: 700;
  margin: 15px 0 15px 20px;

}
#open-sea-dragon:focus {
  outline: none;
  border: none;
}

.clickable-card {
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-left: 0;
}

.clickable-card:hover {
  background-color: #e6f7ff; /* 鼠标悬停时背景变蓝 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 鼠标悬停时添加阴影 */
}

.clickable-card-active {
  background-color: #66b1ff !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.slice-checkbox {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 0;
}

#comment-card {
  width: 300px;
  height: 80px;
  top: 0;
  left: 0;
  position: absolute;
  transition-property: transform;
  transition-duration: 0.1s;
  transition-timing-function: linear;
}
</style>

<style>
.slice-card .el-card__header, .slice-card .el-card__body {
  padding-left: 10px;
  padding-right: 10px;
}
.slice-card .el-card__body {
  padding-top: 10px;
  padding-bottom: 0;
}
.clickable-card .el-card__body {
  padding-top: 8px;
  padding-bottom: 8px;
  padding-left: 10px;
}
</style>

<script lang="ts">
import { ElMessage, ElLoading } from 'element-plus';
import { UploadFilled } from '@element-plus/icons-vue'
// @ts-ignore
import { getSliceList, uploadSlice } from '@/api/slice' // 导入 API 方法
import { mapGetters } from 'vuex';
export default {
  components: {
    UploadFilled
  },
  data(){
    return {
      isDialogVisible: false,  // 控制 上传对话框 显示的变量
      uploadForm: {
        // sliceName: '',     // 切片名称
        // fileList: []       // 上传的文件列表
        path: 'C:\\svs\\', // 默认路径，可以为空
      },
      isUploading: false, // 控制全局加载状态
      rules: {
        // sliceName: [{ required: true, message: '请输入切片名称', trigger: 'blur' }],
        path: [{ required: true, message: '请填写文件路径', trigger: 'blur' }],
        // fileList: [{ required: true, message: '请上传切片文件', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapGetters('appPage', ['getItemTitle']),
    itemTitle() {
      console.log("获取Vuex中的item.title:", this.getItemTitle);
      return this.getItemTitle; // 获取 Vuex 中的 item.title
    },
    appId() {
      return this.$store.getters['appPage/getAppId']; // 从 Vuex 中获取 appId
    }
  },
  methods: {
    // 提交上传
     // 校验路径格式（基本校验）
     validatePathFormat() {
      const pathRegex = /^[a-zA-Z]:\\(?:[^\\/:*?"<>|\r\n]+\\)*$/;
      if (!pathRegex.test(this.uploadForm.path)) {
        ElMessage.error('路径格式不正确，请输入有效的文件夹路径(注意结尾为 \\")');
        return false;
      }
      return true;
    },
    async handleSubmit() {
      this.$refs.uploadForm.validate(async (valid) => {
        /*
        当你使用 v-model 绑定一个输入框的值（如路径）时，Vue 会自动将用户输入的值传递为 JavaScript 字符串。
        反斜杠 \ 在 HTML 输入中不会触发任何转义，它会被原样传递到 JavaScript 中。
        */
        // console.log('this.uploadForm.path:', this.uploadForm.path);
        // console.log('优化后:',this.normalizePath(this.uploadForm.path));
        // return;
        if (!valid) {
          ElMessage.error('请填写完整表单');
          return;
        }
         // 调用后端 API 校验路径是否有效
        const isValidPath = this.validatePathFormat(this.uploadForm.path);
        if (!isValidPath) {
          return;
        }
        this.isUploading = true;
        try {
          const formData = new FormData();
           // 调用 normalizePath 方法处理路径
          const formattedPath = this.uploadForm.path;
          formData.append('path', formattedPath);
          formData.append('applicationld', this.appId);
          const response = await uploadSlice(formData);  // 调用 API
          ElMessage.success(response.msg || '上传成功');
          this.isDialogVisible = false;
          // this.fetchDataList();  // 刷新列表
        } catch (error) {
          ElMessage.error('上传失败，请重试');
        } finally {
          this.isUploading = false;
        }
      })

    },
    // 格式化路径，将 \ 转换为 /
    normalizePath(inputPath) {
      // 处理 Windows 路径中的反斜杠转为 Unix 风格
      return inputPath.replace(/\\/g, '/');
    },
    openUploadDialog() {
      this.isDialogVisible = true;
      console.log('打开上传对话框:', this.isDialogVisible);
    },
    handleDialogOpen() {
      console.log('Dialog 已打开');
    },

    handleDialogClose(done) {
      if (this.isUploading) {
        ElMessage.warning("正在上传，请稍候...");
      } else {
        done();
      }
    },
    validateFile(file) {
      const validTypes = ['image/png', 'image/jpeg', 'image/jpg'];
      const extension = file.name.split('.').pop().toLowerCase();
      console.log("validateFile中文件类型:", file.raw.type);
      const isValidType = validTypes.includes(file.raw.type) || extension === 'svs';

      if (!isValidType) {
        this.$message.error('文件类型不支持！只允许上传 .svs, .png, .jpg 类型的文件。');
        return false;
      }
      if (file.size > 8 * 1024 * 1024) {
        this.$message.error(`${file.name} 文件大小不能超过 8MB`);
        return false;
      }
      return true;
    },
    //on-remove
    handleFileRemove(file, fileList) {
      console.log('移除文件:', file, fileList);
      this.uploadForm.fileList = fileList;
      console.log("移除后的文件列表:", this.uploadForm.fileList);
    },
    handleChange(file, fileList) {
      // 当有新文件被添加时触发
      console.log("处理新文件上传:", file, fileList);
      const isValid = this.validateFile(file);
      if (!isValid) {
        // 文件不符合要求时，从 fileList 中移除该文件
        const index = fileList.indexOf(file);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      }
      // 只有this.uploadForm.fileList变化或者说赋值的时候才会改变...el-upload的list...
      // 所以,下面这个必须要有，否则无法显示或者删除
      this.uploadForm.fileList = fileList;
      console.log("处理后uploadForm文件列表:", this.uploadForm.fileList);
    },
    //on-exceed
    exceedFile(files, fileList) {
      this.$message.warning('文件数量超出限制');
    },
  },
  watch: {
    // 监听 isUploading 变量
    isUploading(newValue) {
      if (newValue) {
        // 如果 isUploading 为 true，开启全局加载
        this.loadingInstance = ElLoading.service({
          lock: true,
          text: '正在加载...',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      } else if (this.loadingInstance) {
        // 如果 isUploading 为 false，关闭全局加载
        this.loadingInstance.close();
      }
    }
  },

}
</script >